package com.example.flashcard.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.HourMinute
import com.example.flashcard.data.SessionInfo
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.dao.SessionDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.SessionEntity
import com.example.flashcard.model.entities.calculateDueDate
import com.example.flashcard.screens.study.CardState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class StudyViewModel(
	private val cardDao: CardDao,
	private val collectionDao: CollectionDao,
	private val sessionDao: SessionDao
) : ViewModel() {
	var cards: List<CardEntity> = emptyList()
		private set
	
	fun setStudyCollection(collectionId: Long) {
		viewModelScope.launch {
			cards = cardDao.getDueCards(collectionId)
			// check if the cards list is empty
			if (cards.isEmpty()) {
				Log.e("StudyViewModel", "Cards List is Empty.")
			}
		}
	}
	
	fun setAllCards() {
		viewModelScope.launch {
			cards = cardDao.getAllCards()
		}
	}
	
	fun removeCurrentCardFromList(
		cardsList: MutableState<List<CardEntity>>,
		updateCardState: (CardState) -> Unit,
		QuestionState: CardState
	) {
		val tempCardList = cardsList.value.toMutableList()
		tempCardList.removeAt(0) // remove the current card from the list
		updateCardState(QuestionState) // flip the card
		cardsList.value = tempCardList
	}
	
	fun moveToNextBox(
		card: CardEntity
	) {
		val lastReviewDate = System.currentTimeMillis()
		val dueDate = calculateDueDate(card.boxNumber + 1, lastReviewDate)
		Log.d("CardScreen", "Due Date: $dueDate")
		var isMasteredState = 0
		if (card.boxNumber + 1 == 7) {
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
		Log.d("CardScreen", "Due Date: $dueDate")
		
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
			Log.d("StudyViewModel", "Cleared Database.")
		}
	}
	
	// Collection Screen(progress bar)
	
	private suspend fun getProgress(collectionId: Long): Float {
		val totalCollectionCardsCount: Long
		val totalCollectionMasteredCardsCount: Long
		
		withContext(Dispatchers.IO) {
			totalCollectionCardsCount = cardDao.getCollectionCardsCount(collectionId)
			Log.d("Progress", "fetched total: $totalCollectionCardsCount")
			totalCollectionMasteredCardsCount =
				cardDao.getCollectionMasteredCardsCount(collectionId)
			Log.d("Progress", "fetched mastered: $totalCollectionMasteredCardsCount")
		}
		Log.d(
			"Progress",
			"Returned P: ${totalCollectionMasteredCardsCount.toFloat() / totalCollectionCardsCount}"
		)
		
		return totalCollectionMasteredCardsCount.toFloat() / totalCollectionCardsCount
	}
	
	fun updateProgress(collectionId: Long) {
		viewModelScope.launch {
			val progress = getProgress(collectionId)
			Log.d("Progress", "Progress Value: $progress")
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
	
	
}