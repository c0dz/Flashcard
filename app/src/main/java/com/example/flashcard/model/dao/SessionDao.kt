package com.example.flashcard.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcard.model.entities.SessionEntity

@Dao
interface SessionDao {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertSession(session: SessionEntity): Long
	
	
	@Query("DELETE FROM sessions;")
	suspend fun deleteAllSessions()
}