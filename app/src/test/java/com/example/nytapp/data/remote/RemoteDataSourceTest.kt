package com.example.nytapp.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nytapp.MainCoroutineRule
import com.example.nytapp.SampleTestData
import com.example.nytapp.helpers.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class RemoteDataSourceTest {
    @get:Rule
    var instantTaskExecutorRue = InstantTaskExecutorRule()

    @get:Rule
    var mainTestDispatcher = MainCoroutineRule()

    private lateinit var remoteDataSource: RemoteDataSource
    private val apiService = mockk<ApiService>()

    @Before
    fun setup() {
        remoteDataSource = RemoteDataSource(apiService)
    }

    @Test
    fun `verify that when call to network is successful, Resource Success is returned`() =
        runBlocking {
            coEvery { apiService.getTopStories() } returns SampleTestData.sampleStoryResponse
            val result = remoteDataSource.fetchAllStories()
            assertEquals(result, result as Resource.Success)
            assertEquals(result.data, SampleTestData.sampleStoryResponse)
            coVerify { apiService.getTopStories() }
        }

    @Test
    fun `verify that when call to network is not successful, Resource Error is returned`() =
        runBlocking {
            coEvery { apiService.getTopStories() } coAnswers { throw Exception("something went wrong") }
            val result = remoteDataSource.fetchAllStories()
            assertEquals(result, result as Resource.Error)
            assertEquals(result.message, "something went wrong")
            coVerify { apiService.getTopStories() }
        }
}