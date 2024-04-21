package com.example.flashcard.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.flashcard.model.entities.CardEntity

@Dao
interface CardDao {
	
	@Insert
	suspend fun insertCard(card: CardEntity)
	
	@Delete
	suspend fun deleteCard(card: CardEntity)
}