package com.example.nytapp.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.nytapp.data.local.LocalDataSource
import com.example.nytapp.data.mappers.TopStoryResponseMapper
import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.data.remote.RemoteDataSource
import com.example.nytapp.helper.Resource
import com.example.nytapp.MainCoroutineRule
import com.example.nytapp.SampleTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopStoryRepositoryTest {
    @get:Rule
    var instantTaskExecutorRue = InstantTaskExecutorRule()

    @get:Rule
    var mainTestDispatcher = MainCoroutineRule()

    private lateinit var repository: TopStoryRepository
    private val localDataSource = mockk<LocalDataSource>()
    private val remoteDataSource = mockk<RemoteDataSource>()
    private val mapper = mockk<TopStoryResponseMapper>()

    @Before
    fun setup() {
        repository = TopStoryRepository(localDataSource, remoteDataSource, mapper)
    }

    @Test
    fun `verify that when fetchTopStories is successful, local data is cached and success Resource is returned`() =
        runBlocking {
            coEvery { remoteDataSource.fetchAllStories() } returns Resource.Success(SampleTestData.sampleStoryResponse)
            every { mapper.invoke(any()) } returns SampleTestData.sampleStoryData[0]
            coEvery { localDataSource.saveAllStories(SampleTestData.sampleStoryData.take(1)) } returns Unit

            val result = repository.fetchTopStories()

            assertEquals(result, result as Resource.Success)
            coVerify { remoteDataSource.fetchAllStories() }
            coVerify { localDataSource.saveAllStories(SampleTestData.sampleStoryData.take(1)) }
        }

    @Test
    fun `verify that when fetchTopStories is not successful, data is not persisted and error status Resource is returned`() =
        runBlocking {
            coEvery { remoteDataSource.fetchAllStories() } returns Resource.Error("something went wrong")

            val result = repository.fetchTopStories()

            assertEquals(result, result as Resource.Error)
            coVerify { remoteDataSource.fetchAllStories() }
            coVerify(exactly = 0) { localDataSource.saveAllStories(any()) }
        }

    @Test
    fun `verify that when local data source is empty, localsource returns stream of empty data `() =
        runBlocking {
            coEvery { localDataSource.getAllStories() } returns flowOf(emptyList())

            val result = repository.collectStoryData()

            coVerify { localDataSource.getAllStories() }
            result.test {
                assertEquals(awaitItem(), emptyList<TopStoryData>())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `verify that when local data source is not, localsource returns stream of available data `() =
        runBlocking {
            coEvery { localDataSource.getAllStories() } returns flowOf(SampleTestData.sampleStoryData)

            val result = repository.collectStoryData()

            coVerify { localDataSource.getAllStories() }
            result.test {
                assertEquals(awaitItem(), SampleTestData.sampleStoryData)
                cancelAndIgnoreRemainingEvents()
            }
        }
}