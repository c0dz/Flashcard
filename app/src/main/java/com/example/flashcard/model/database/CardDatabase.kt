package com.example.flashcard.model.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.flashcard.model.DateConverters
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.CollectionEntity

@Database(
	entities = [CardEntity::class, CollectionEntity::class],
	version = 8,
	autoMigrations = [
		AutoMigration(
			from = 7,
			to = 8,
//			spec = CardDatabaseMigration6To7::class
		)
	],
	exportSchema = true,
)
@TypeConverters(DateConverters::class)
abstract class CardDatabase : RoomDatabase() {
	
	abstract val cardDao: CardDao
	abstract val collectionDao: CollectionDao
	
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
						//.addMigrations(Migration6To7)
						.build()
					
					INSTANCE = instance
				}
				
				return instance
			}
		}
	}
}

//
//val Migration6To7 = object : Migration(1, 2) {
//	override fun migrate(db: SupportSQLiteDatabase) {
//		db.execSQL("ALTER TABLE cards ADD COLUMN box_number INTEGER DEFAULT 1 not null");
//		//db.execSQL("ALTER TABLE cards ADD COLUMN last_review_date TEXT NOT NULL")
//		//db.execSQL("ALTER TABLE cards ADD COLUMN due_date TEXT NOT NULL")
//	}
//}