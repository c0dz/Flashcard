package com.example.flashcard.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcard.model.entities.CollectionEntity

@Dao
interface CollectionDao {
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertCollection(card: CollectionEntity)
	
	@Delete
	suspend fun deleteCollection(card: CollectionEntity)
	
	@Query("SELECT * FROM collections")
	suspend fun getAllCollections(): List<CollectionEntity>
	
	// get the last inserted collection id
	@Query("SELECT id FROM collections ORDER BY id DESC LIMIT 1")
	suspend fun getLastCollectionId(): Int
}