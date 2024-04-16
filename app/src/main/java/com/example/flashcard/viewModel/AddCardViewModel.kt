package com.example.flashcard.viewModel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.flashcard.data.CardData

class AddCardViewModel : ViewModel() {
	var cardList = mutableStateListOf<CardData>()
		private set
	
	private val nextCardId = mutableIntStateOf(1)
	
	fun addCard(cardQuestion: String, cardAnswer: String) {
		val nextCard = CardData(
			id = nextCardId.intValue,
			question = cardQuestion,
			answer = cardAnswer
		)
		
		cardList.add(nextCard)
		nextCardId.intValue++
	}
	
	fun modifyQuestion(cardQuestion: String, cardId: Int) {
		cardList.find { it.id == cardId }?.question = cardQuestion
	}
	
	fun modifyAnswer(cardAnswer: String, cardId: Int) {
		cardList.find { it.id == cardId }?.answer = cardAnswer
	}
}