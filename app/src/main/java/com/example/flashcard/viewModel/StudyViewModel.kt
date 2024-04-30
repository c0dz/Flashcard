package com.example.flashcard.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.calculateDueDate
import com.example.flashcard.screens.study.CardState
import kotlinx.coroutines.launch

class StudyViewModel(
	private val cardDao: CardDao,
	private val collectionDao: CollectionDao
) : ViewModel() {
	var cards: List<CardEntity> = emptyList()
		private set
	
	fun setStudyCollection(collectionId: Long) {
		viewModelScope.launch {
			Log.d("StudyViewModel", "setStudyCollection: $collectionId")
			Log.d("StudyViewModel", "STARTED SETTING STUDY COLLECTION")
			cards = cardDao.getAllCards(collectionId)
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
		
		val updatedCard = CardEntity(
			id = card.id,
			question = card.question,
			answer = card.answer,
			collectionId = card.collectionId,
			boxNumber = card.boxNumber + 1,
			lastReviewDate = lastReviewDate,
			dueDate = calculateDueDate(card.boxNumber + 1, lastReviewDate)
		)
		
		viewModelScope.launch {
			cardDao.upsertCard(updatedCard)
		}
	}
}