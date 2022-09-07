package com.example.nytapp.data.remote

import com.example.nytapp.data.model.TopStoriesResponse
import com.example.nytapp.helpers.Resource
import com.example.nytapp.helpers.makeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchAllStories(): Resource<TopStoriesResponse> {
        return withContext(Dispatchers.IO) {
            makeCall { apiService.getTopStories() }
        }
    }
}