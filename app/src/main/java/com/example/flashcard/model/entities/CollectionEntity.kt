package com.example.flashcard.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "collections",
)
data class CollectionEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	@ColumnInfo(name = "collection_name")
	var name: String = "",
	var tags: String = "",
	var description: String = "",
)