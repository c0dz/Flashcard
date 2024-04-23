package com.example.flashcard.viewModel

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao

class StudyViewModel(
	private val cardDao: CardDao,
	private val collectionDao: CollectionDao
) : ViewModel() {
	val currentStudyCollection = mutableIntStateOf(0)
	
}