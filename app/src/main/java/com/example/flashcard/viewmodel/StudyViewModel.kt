package com.example.flashcard.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.HourMinute
import com.example.flashcard.data.SessionInfo
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.dao.SessionDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.CollectionEntity
import com.example.flashcard.model.entities.SessionEntity
import com.example.flashcard.model.entities.calculateDueDate
import com.example.flashcard.screens.study.CardState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

/**
 * StudyViewModel class responsible for managing study-related data and operations.
 *
 * This class handles the business logic related to studying, including managing study sessions, updating card progress,
 * calculating study statistics, and handling database operations.
 *
 * @param cardDao The Data Access Object (DAO) for card entities, used for database operations related to cards.
 * @param collectionDao The Data Access Object (DAO) for collection entities, used for database operations related to collections.
 * @param sessionDao The Data Access Object (DAO) for session entities, used for database operations related to study sessions.
 */
class StudyViewModel(
	private val cardDao: CardDao,
	private val collectionDao: CollectionDao,
	private val sessionDao: SessionDao
) : ViewModel() {
	var cards: List<CardEntity> = emptyList()
		private set
	
	
	suspend fun setStudySession(collectionId: Long) {
		withContext(Dispatchers.IO) {
			cards = cardDao.getDueCards(collectionId)
		}
	}
	
	suspend fun setMasteredStudySession(collectionId: Long) {
		withContext(Dispatchers.IO) {
			cards = cardDao.getCollectionCards(collectionId)
		}
	}
	
	fun setAllCards() {
		viewModelScope.launch {
			cards = cardDao.getAllDueCards()
		}
	}
	
	fun removeCurrentCardFromList(
		cardsList: MutableState<List<CardEntity>>,
		updateCardState: (CardState) -> Unit,
		questionState: CardState
	) {
		val tempCardList = cardsList.value.toMutableList()
		tempCardList.removeAt(0) // remove the current card from the list
		updateCardState(questionState) // flip the card
		cardsList.value = tempCardList
	}
	
	fun moveToNextBox(
		card: CardEntity
	) {
		val lastReviewDate = System.currentTimeMillis()
		val dueDate = calculateDueDate(card.boxNumber + 1, lastReviewDate)
		var isMasteredState = 0
		if (card.boxNumber + 1 >= 7) {
			isMasteredState = 1
		}
		
		val updatedCard = CardEntity(
			id = card.id,
			question = card.question,
			answer = card.answer,
			collectionId = card.collectionId,
			boxNumber = card.boxNumber + 1,
			lastReviewDate = lastReviewDate,
			dueDate = dueDate,
			isMastered = isMasteredState
		)
		
		viewModelScope.launch {
			cardDao.upsertCard(updatedCard)
		}
	}
	
	fun moveToFirstBox(
		card: CardEntity
	) {
		val lastReviewDate = System.currentTimeMillis()
		val dueDate = calculateDueDate(0, lastReviewDate)
		
		val updatedCard = CardEntity(
			id = card.id,
			question = card.question,
			answer = card.answer,
			collectionId = card.collectionId,
			boxNumber = 0,
			lastReviewDate = lastReviewDate,
			dueDate = dueDate,
			isMastered = 0
		)
		
		viewModelScope.launch {
			cardDao.upsertCard(updatedCard)
		}
	}
	
	
	// Clear Data
	fun clearDatabase() {
		viewModelScope.launch {
			cardDao.deleteAllCards()
			collectionDao.deleteAllCollections()
			sessionDao.deleteAllSessions()
		}
	}
	
	suspend fun deleteCollection(collectionId: Long) {
		withContext(Dispatchers.IO) {
			collectionDao.deleteCollection(collectionId)
		}
	}
	
	// Collection Screen(progress bar)
	suspend fun getProgress(collectionId: Long): Float {
		val totalCollectionCardsCount: Long
		val totalCollectionMasteredCardsCount: Long
		
		withContext(Dispatchers.IO) {
			totalCollectionCardsCount = cardDao.getCollectionCardsCount(collectionId)
			totalCollectionMasteredCardsCount =
				cardDao.getCollectionMasteredCardsCount(collectionId)
		}
		
		
		return totalCollectionMasteredCardsCount.toFloat() / totalCollectionCardsCount
	}
	
	fun updateProgress(collectionId: Long) {
		viewModelScope.launch {
			val progress = getProgress(collectionId)
			collectionDao.updateCollectionProgress(collectionId = collectionId, progress = progress)
		}
	}
	
	// Sessions
	var sessionInfo = SessionInfo(
		collectionId = 0,
		startTime = 0,
		endTime = 0,
		cardsNumber = 0,
		failedCards = 0
	)
	
	fun addNewSession() {
		val newSession = SessionEntity(
			startTime = sessionInfo.startTime,
			endTime = sessionInfo.endTime,
			duration = sessionInfo.endTime - sessionInfo.startTime,
			cardsReviewed = sessionInfo.cardsNumber,
			cardsFailed = sessionInfo.failedCards,
			score = ((1 - (sessionInfo.failedCards.toDouble() / sessionInfo.cardsNumber)) * 100).toInt()
		)
		viewModelScope.launch {
			sessionDao.insertSession(newSession)
		}
		SessionInfo.setToDefault(sessionInfo) // reset value
	}
	
	
	// Progress
	private var totalCardsCount: Long = 0
	private var masteredCardsCount: Long = 0
	private var successRate: Double = 0.0
	private var timeSpent: Long = 0
	
	fun getTotalCardCount(): Long {
		viewModelScope.launch {
			totalCardsCount = cardDao.getTotalCardCount()
		}
		return totalCardsCount
	}
	
	fun getMasteredCardsCount(): Long {
		viewModelScope.launch {
			masteredCardsCount = cardDao.getMasteredCardCount()
		}
		return masteredCardsCount
	}
	
	fun getSuccessRate(): Double {
		viewModelScope.launch {
			successRate = sessionDao.getAverageScore()
		}
		return String.format("%.2f", successRate).toDouble()
	}
	
	fun getTimeSpent(): HourMinute {
		viewModelScope.launch {
			timeSpent = sessionDao.getTotalDuration()
		}
		val hours = (timeSpent.toDouble() / 3600000).toInt()
		val minutes = ((timeSpent.toDouble() % 3600000) / 60000).toInt()
		val seconds = ((timeSpent.toDouble() % 60000) / 1000).toInt()
		
		return HourMinute(
			hour = hours,
			minute = minutes,
			second = seconds
		)
	}
	
	// Streak
	private var sessions: List<SessionEntity> = listOf()
	fun getStreak(): Int {
		viewModelScope.launch {
			sessions = sessionDao.getAllSessions()
		}
		if (sessions.isEmpty()) return 0
		
		// Sort sessions by start time
		val sortedSessions = sessions.sortedBy { it.startTime }
		
		var streakCount = 1 // At least one session is needed for a streak
		var currentStreakLength = 1
		var previousDate: Date? = Date(sortedSessions.first().startTime ?: return 0)
		
		sortedSessions.drop(1).forEach { session ->
			val currentDate = Date(session.startTime ?: return@forEach)
			
			// Check if the current date is consecutive with the previous date
			if (isConsecutive(previousDate!!, currentDate)) {
				currentStreakLength++
				streakCount = maxOf(streakCount, currentStreakLength)
			} else {
				// Start a new streak
				currentStreakLength = 1
			}
			
			previousDate = currentDate
		}
		
		return streakCount
	}
	
	private fun isConsecutive(date1: Date, date2: Date): Boolean {
		val calendar = Calendar.getInstance()
		calendar.time = date1
		calendar.add(Calendar.DAY_OF_YEAR, 1)
		return calendar.time == date2
	}
	
	
	// get All tables
	suspend fun getAllCards(): List<CardEntity> {
		val cards: List<CardEntity>
		withContext(Dispatchers.IO) {
			cards = cardDao.getAllCards()
		}
		return cards
	}
	
	suspend fun getAllCollections(): List<CollectionEntity> {
		val collections: List<CollectionEntity>
		withContext(Dispatchers.IO) {
			collections = collectionDao.getCollections()
		}
		return collections
	}
	
	suspend fun getAllSessions(): List<SessionEntity> {
		val sessions: List<SessionEntity>
		withContext(Dispatchers.IO) {
			sessions = sessionDao.getAllSessions()
		}
		return sessions
	}
	
}