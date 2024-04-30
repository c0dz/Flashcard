package com.example.flashcard.model.entities

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
	tableName = "cards",
	foreignKeys = [
		ForeignKey(
			entity = CollectionEntity::class,
			parentColumns = ["id"],
			childColumns = ["collection_id"],
			onDelete = CASCADE,
			onUpdate = CASCADE
		)
	]
)
data class CardEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	var question: String = "",
	var answer: String = "",
	@ColumnInfo(name = "collection_id", index = true)
	var collectionId: Long,
	@ColumnInfo(name = "box_number", defaultValue = "1")
	var boxNumber: Int = 0,
	@ColumnInfo(name = "last_review_date")
	var lastReviewDate: Long?,
	@ColumnInfo(name = "due_date")
	var dueDate: Long?
)

fun calculateDueDate(boxNumber: Int, lastReviewDate: Long = System.currentTimeMillis()): Long {
	val intervalInDays = when (boxNumber) {
		0 -> 0
		1 -> 1
		2 -> 3
		3 -> 7
		4 -> 14
		5 -> 30
		6 -> 45
		7 -> 60
		else -> 90
	}
	Log.d("Card", "Interval: $intervalInDays")
	
	return lastReviewDate + intervalInDays * 24 * 60 * 60 * 1000
}