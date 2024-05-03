package com.example.flashcard.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.flashcard.model.entities.CardEntity

@Dao
interface CardDao {
	
	@Upsert()
	suspend fun upsertCard(card: CardEntity)
	
	@Delete
	suspend fun deleteCard(card: CardEntity)
	
	@Query("SELECT * FROM cards WHERE collection_id = :collectionId and is_mastered = 0")
	suspend fun getAllCards(collectionId: Long): List<CardEntity>
	
	@Query("SELECT * FROM cards WHERE due_date < :currentDate and collection_id = :collectionId and is_mastered = 0")
	suspend fun getDueCards(
		collectionId: Long,
		currentDate: Long = System.currentTimeMillis()
	): List<CardEntity>
	
	@Query("DELETE FROM cards;")
	suspend fun deleteAllCards()
	
	@Query("SELECT COUNT(*) FROM cards;")
	suspend fun getTotalCardCount(): Long
	
	@Query("SELECT COUNT(*) FROM cards WHERE is_mastered = 1;")
	suspend fun getMasteredCardCount(): Long
}