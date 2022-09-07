package com.example.nytapp.data.local

import android.content.Context
import androidx.room.*
import com.example.nytapp.data.model.TopStoryData

@Database(
    entities = [TopStoryData::class],
    version = 1,
    exportSchema = false
)
abstract class TopStoryDatabase : RoomDatabase() {
    abstract fun storyDao(): TopStoryDao

    companion object {
        private var instance: TopStoryDatabase? = null

        fun getDatabase(context: Context): TopStoryDatabase {
            if (instance == null) {
                synchronized(TopStoryDatabase::class.java) {}
                instance =
                    Room.databaseBuilder(context, TopStoryDatabase::class.java, "AppDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
            }

            return instance!!
        }
    }
}
