package com.example.flashcard.viewModel

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.CardData
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.CollectionEntity
import kotlinx.coroutines.launch

class CardViewModel(
	private val cardDao: CardDao,
	private val collectionDao: CollectionDao
) : ViewModel() {
	var cardList = mutableStateListOf<CardData>()
		private set
	
	private val nextCardId = mutableIntStateOf(1)
	
	// Collection details
	private var collectionName = mutableStateOf("")
	private var tags = mutableStateOf("")
	private var description = mutableStateOf("")
	
	
	fun getCollectionName(): String {
		return collectionName.value
	}
	
	fun setCollectionName(collectionName: String) {
		this.collectionName.value = collectionName
	}
	
	fun getTags(): String {
		return tags.value
	}
	
	fun setTags(tags: String) {
		this.tags.value = tags
	}
	
	fun getDescription(): String {
		return description.value
	}
	
	fun setDescription(description: String) {
		this.description.value = description
	}
	
	
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
	
	// Database operations
	fun insertCollectionToDB() {
		val collection = CollectionEntity(
			name = collectionName.value,
			tags = tags.value,
			description = description.value
		)
		viewModelScope.launch {
			try {
				collectionDao.insertCollection(collection)
			} catch (e: SQLiteConstraintException) {
				println("Error inserting collection: ${e.message}")
			}
			//collectionDao.insertCollection(collection)
		}
	}
	
	fun getAllCollectionsFromDB() {
		viewModelScope.launch {
			val collections = collectionDao.getAllCollections()
			collections.forEach { collection ->
				println("Collection Name: ${collection.name}")
			}
		}
	}
	
	fun insertCardToDB(card: CardEntity) {
		viewModelScope.launch {
			cardDao.insertCard(card)
		}
	}
}