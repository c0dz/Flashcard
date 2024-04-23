package com.example.flashcard.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.flashcard.data.QuestionAnswer
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class StudyViewModel(
	private val cardDao: CardDao,
	private val collectionDao: CollectionDao
) : ViewModel() {
	private val currentStudyCollection: MutableList<QuestionAnswer> =
		mutableStateListOf()
	
	var cards: Flow<List<QuestionAnswer>> = emptyFlow()
	fun setStudyCollection(collectionId: Long) {
		cards = cardDao.getAllCards(collectionId)
	}
	
	fun getCurrentStudyCards(): Flow<List<QuestionAnswer>> {
		return cards
	}
	
}