package com.example.flashcard.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.flashcard.data.QuestionAnswer
import com.example.flashcard.model.entities.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
	
	@Insert
	suspend fun insertCard(card: CardEntity)
	
	@Delete
	suspend fun deleteCard(card: CardEntity)
	
	@Query("SELECT question, answer FROM cards WHERE collection_id = :collectionId")
	fun getAllCards(collectionId: Long): Flow<List<QuestionAnswer>>
}