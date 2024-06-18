package com.example.flashcard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.dao.SessionDao

class AppViewModelFactory(
	private val cardDao: CardDao,
	private val collectionDao: CollectionDao,
	private val sessionDao: SessionDao
) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return when {
			modelClass.isAssignableFrom(CardViewModel::class.java) ->
				CardViewModel(cardDao, collectionDao) as T
			
			modelClass.isAssignableFrom(StudyViewModel::class.java) ->
				StudyViewModel(cardDao, collectionDao, sessionDao) as T
			
			else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
		}
	}
}