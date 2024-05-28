package com.example.flashcard.viewModel

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.CardData
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.CollectionEntity
import com.example.flashcard.model.entities.calculateDueDate
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for managing card-related data and operations.
 *
 * This class handles the business logic related to managing cards, including adding, modifying, and inserting cards into the database.
 * It also manages temporary data such as collection details and maintains a list of cards for display.
 *
 * @param cardDao The Data Access Object (DAO) for card entities, used for database operations related to cards.
 * @param collectionDao The Data Access Object (DAO) for collection entities, used for database operations related to collections.
 */
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
	
	val collections = collectionDao.getAllCollections()
	
	// Database operations
	private var createdCollectionID = mutableLongStateOf(0)
	
	fun insertCollectionToDB() {
		val collection = CollectionEntity(
			name = collectionName.value,
			tags = tags.value,
			description = description.value
		)
		viewModelScope.launch {
			try {
				createdCollectionID.longValue = collectionDao.insertCollection(collection)
			} catch (e: SQLiteConstraintException) {
				Log.e("CardViewModel", "Error inserting collection: ${e.message}")
			}
		}
	}
	
	fun insertCardToDB() {
		viewModelScope.launch {
			val lastReviewDateDefault = System.currentTimeMillis()
			val dueDateDefault = calculateDueDate(0, lastReviewDateDefault)
			
			val cardListEntity = cardList.map { cardData ->
				CardEntity(
					question = cardData.question,
					answer = cardData.answer,
					collectionId = createdCollectionID.longValue,
					lastReviewDate = lastReviewDateDefault,
					dueDate = dueDateDefault,
					isMastered = 0
				)
			}
			
			cardListEntity.forEach { cardEntity ->
				cardDao.upsertCard(cardEntity)
			}
			
		}
	}
	
	
	fun clearTempData() {
		this.description.value = ""
		this.tags.value = ""
		this.collectionName.value = ""
		
		cardList.clear()
		this.nextCardId.intValue = 1
	}
}