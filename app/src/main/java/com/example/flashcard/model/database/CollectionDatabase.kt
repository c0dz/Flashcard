package com.example.flashcard.model.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.entities.CollectionEntity

@Database(
	entities = [CollectionEntity::class],
	version = 2,
	autoMigrations = [
		AutoMigration(
			from = 1,
			to = 2,
			spec = CollectionDatabaseMigration1To2::class
		)
	],
	exportSchema = true,
)
abstract class CollectionDatabase : RoomDatabase() {
	
	abstract val collectionDao: CollectionDao
	
	companion object {
		private const val DATABASE_NAME = "collection_database.db"
		
		@Volatile
		private var INSTANCE: CollectionDatabase? = null
		
		fun getInstance(context: Context): CollectionDatabase {
			synchronized(this) {
				var instance = INSTANCE
				
				if (instance == null) {
					instance = Room.databaseBuilder(
						context = context.applicationContext,
						CollectionDatabase::class.java,
						DATABASE_NAME
					).build()
					
					INSTANCE = instance
				}
				
				return instance
			}
		}
	}
}

@RenameColumn(tableName = "collections", fromColumnName = "name", toColumnName = "collection_name")
class CollectionDatabaseMigration1To2 : AutoMigrationSpec