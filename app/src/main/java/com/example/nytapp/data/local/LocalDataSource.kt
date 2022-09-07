package com.example.nytapp.data.local

import com.example.nytapp.data.model.TopStoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocalDataSource @Inject constructor(
    private val topStoryDao: TopStoryDao
) {
    private val coroutineContext: CoroutineContext = Dispatchers.IO

    suspend fun saveAllStories(stories: List<TopStoryData>) {
        withContext(coroutineContext) {
            topStoryDao.saveStories(stories)
        }
    }

    suspend fun getStoryDataById(id: String): TopStoryData? {
        return withContext(coroutineContext) {
            topStoryDao.getStoryById(id)
        }
    }

    fun getAllStories(): Flow<List<TopStoryData>> {
        return topStoryDao.getAllStories()
    }
}