package com.example.flashcard.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "collections",
	indices = [Index(value = ["collection_name"], unique = true)]
)
data class CollectionEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	@ColumnInfo(name = "collection_name")
	var name: String = "",
	var tags: String = "",
	var description: String = "",
)