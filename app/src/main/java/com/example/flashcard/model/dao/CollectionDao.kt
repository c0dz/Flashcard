package com.example.flashcard.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcard.model.entities.CollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertCollection(card: CollectionEntity): Long
	
	@Delete
	suspend fun deleteCollection(card: CollectionEntity)
	
	@Query("SELECT * FROM collections")
	fun getAllCollections(): Flow<List<CollectionEntity>>
	
	@Query("DELETE FROM collections;")
	suspend fun deleteAllCollections()
	
	@Query("UPDATE collections SET progress = :progress WHERE id = :collectionId;")
	suspend fun updateCollectionProgress(collectionId: Long, progress: Float)
	
}