package com.example.flashcard.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "sessions",
)
data class SessionEntity(
	@PrimaryKey(autoGenerate = true)
	var id: Long = 0,
	@ColumnInfo(name = "start_time")
	var startTime: Long?,
	@ColumnInfo(name = "end_time")
	var endTime: Long?,
	@ColumnInfo(name = "duration")
	var duration: Long?,
	@ColumnInfo(name = "cards_reviewed")
	var cardsReviewed: Long = 0,
	@ColumnInfo(name = "cards_failed")
	var cardsFailed: Long = 0,
	var score: Int = 0,
)