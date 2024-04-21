package com.example.flashcard.model.entities

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
	val id: Int = 0,
	var question: String,
	var answer: String,
	@ColumnInfo(name = "collection_id", index = true)
	var collectionId: Int
)