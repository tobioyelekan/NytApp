package com.example.nytapp.data.remote

import com.example.nytapp.data.model.TopStoriesResponse
import retrofit2.http.GET

interface ApiService {
    @GET("arts.json")
    suspend fun getTopStories(): TopStoriesResponse
}