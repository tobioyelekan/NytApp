package com.example.nytapp.repositories

import com.example.nytapp.data.local.LocalDataSource
import com.example.nytapp.data.mappers.TopStoryResponseMapper
import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.data.remote.RemoteDataSource
import com.example.nytapp.helper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TopStoryRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val mapper: TopStoryResponseMapper
) {
    suspend fun fetchTopStories() =
        when (val result = remoteDataSource.fetchAllStories()) {
            is Resource.Success -> {
                result.data?.let { data ->
                    localDataSource.saveAllStories(data.results.map { mapper(it) })
                    Resource.Success(Unit)
                }
            }
            else -> result
        }

    fun collectStoryData(): Flow<List<TopStoryData>> {
        return localDataSource.getAllStories()
    }
}