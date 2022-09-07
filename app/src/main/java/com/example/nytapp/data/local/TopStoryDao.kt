package com.example.nytapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nytapp.data.model.TopStoryData
import kotlinx.coroutines.flow.Flow

@Dao
interface TopStoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStories(stories: List<TopStoryData>)

    @Query("SELECT * FROM top_story_table")
    fun getAllStories(): Flow<List<TopStoryData>>

    @Query("SELECT * FROM top_story_table WHERE id=:id")
    suspend fun getStoryById(id: String): TopStoryData?
}