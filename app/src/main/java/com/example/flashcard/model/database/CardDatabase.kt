package com.example.flashcard.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.CollectionEntity

@Database(
	entities = [CardEntity::class, CollectionEntity::class],
	version = 1,
	exportSchema = false
)
abstract class CardDatabase : RoomDatabase() {
	
	abstract val cardDao: CardDao
	
	companion object {
		private const val DATABASE_NAME = "card_database.db"
		
		@Volatile
		private var INSTANCE: CardDatabase? = null
		
		fun getInstance(context: Context): CardDatabase {
			synchronized(this) {
				var instance = INSTANCE
				
				if (instance == null) {
					instance = Room.databaseBuilder(
						context = context.applicationContext,
						CardDatabase::class.java,
						DATABASE_NAME
					).build()
					
					INSTANCE = instance
				}
				
				return instance
			}
		}
	}
}