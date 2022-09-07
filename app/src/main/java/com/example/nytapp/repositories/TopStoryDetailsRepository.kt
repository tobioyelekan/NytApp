package com.example.nytapp.repositories

import com.example.nytapp.data.local.LocalDataSource
import com.example.nytapp.data.model.TopStoryData
import javax.inject.Inject

class TopStoryDetailsRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    suspend fun getTopStoryData(id: String): TopStoryData? {
        return localDataSource.getStoryDataById(id)
    }
}