package com.example.flashcard.model.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flashcard.model.DateConverters
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.dao.SessionDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.CollectionEntity
import com.example.flashcard.model.entities.SessionEntity

@Database(
	entities = [CardEntity::class, CollectionEntity::class, SessionEntity::class],
	version = 11,
	autoMigrations = [
		AutoMigration(
			from = 10,
			to = 11,
			//spec = Spec::class
		)
	],
	exportSchema = true,
)
@TypeConverters(DateConverters::class)
abstract class CardDatabase : RoomDatabase() {
	
	abstract val cardDao: CardDao
	abstract val collectionDao: CollectionDao
	abstract val sessionDao: SessionDao
	
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
					)
						.addMigrations(Migration)
						.build()
					
					INSTANCE = instance
				}
				
				return instance
			}
		}
	}
}


val Migration = object : Migration(9, 10) {
	override fun migrate(db: SupportSQLiteDatabase) {
		db.execSQL(
			"""
            CREATE TABLE IF NOT EXISTS sessions (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                start_time INTEGER,
                end_time INTEGER,
                duration INTEGER,
                cards_reviewed INTEGER NOT NULL,
                cards_failed INTEGER NOT NULL,
                score INTEGER NOT NULL
            );
        """.trimIndent()
		)
	}
}

