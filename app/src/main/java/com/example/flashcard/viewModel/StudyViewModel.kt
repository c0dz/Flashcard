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
import kotlinx.coroutines.launch

class StudyViewModel(
	private val cardDao: CardDao,
	private val collectionDao: CollectionDao,
	private val sessionDao: SessionDao
) : ViewModel() {
	var cards: List<CardEntity> = emptyList()
		private set
	
	fun setStudyCollection(collectionId: Long) {
		viewModelScope.launch {
			Log.d("StudyViewModel", "setStudyCollection: $collectionId")
			Log.d("StudyViewModel", "STARTED SETTING STUDY COLLECTION")
			cards = cardDao.getDueCards(collectionId)
			Log.d("StudyViewModel", "Time: ${System.currentTimeMillis()}")
			Log.d("StudyViewModel", "FINISHED SETTING STUDY COLLECTION")
			
			// check if the cards list is empty
			if (cards.isEmpty()) {
				Log.e("StudyViewModel", "Cards List is Empty.")
			}
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
	
	// Sessions
	var sessionInfo = SessionInfo(
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
			Log.d("StudyViewModel", "totalCards: $totalCardsCount")
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
		return successRate
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
}