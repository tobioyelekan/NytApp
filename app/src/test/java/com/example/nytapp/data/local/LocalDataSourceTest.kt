package com.example.nytapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.nytapp.MainCoroutineRule
import com.example.nytapp.SampleTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocalDataSourceTest {
    @get:Rule
    var instantTaskExecutorRue = InstantTaskExecutorRule()

    @get:Rule
    var mainTestDispatcher = MainCoroutineRule()

    private lateinit var localDataSource: LocalDataSource
    private val topStoryDao = mockk<TopStoryDao>()

    @Before
    fun setup() {
        localDataSource = LocalDataSource(topStoryDao)
    }

    @Test
    fun `verify that save all stories is successful`() = runBlocking {
        coEvery { topStoryDao.saveStories(SampleTestData.sampleStoryData) } returns Unit

        val result = localDataSource.saveAllStories(SampleTestData.sampleStoryData)

        assertEquals(result, Unit)
        coVerify { topStoryDao.saveStories(SampleTestData.sampleStoryData) }
    }

    @Test
    fun `verify that getStoryId returns data with an id that exists`() = runBlocking {
        coEvery { topStoryDao.getStoryById("id") } returns SampleTestData.sampleStoryData[0]

        val result = localDataSource.getStoryDataById("id")

        assertNotNull(result)
        assertEquals(result, SampleTestData.sampleStoryData[0])
        coVerify { topStoryDao.getStoryById("id") }
    }

    @Test
    fun `verify that getStoryId returns empty data with an id that doesn't exists`() = runBlocking {
        coEvery { topStoryDao.getStoryById("id") } returns null

        val result = localDataSource.getStoryDataById("id")

        assertNull(result)
        coVerify { topStoryDao.getStoryById("id") }
    }

    @Test
    fun `verify that stream of story data is emitted from room database when subscribed and data is not empty`() =
        runBlocking {
            coEvery { topStoryDao.getAllStories() } returns flowOf(SampleTestData.sampleStoryData)

            val result = localDataSource.getAllStories()

            result.test {
                assertEquals(awaitItem(), SampleTestData.sampleStoryData)
                coVerify { topStoryDao.getAllStories() }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `verify that empty data stream of story data is emitted from room database when subscribed and there are no data stored`() =
        runBlocking {
            coEvery { topStoryDao.getAllStories() } returns flowOf(SampleTestData.sampleStoryData)

            val result = localDataSource.getAllStories()

            result.test {
                assertEquals(awaitItem(), SampleTestData.sampleStoryData)
                coVerify { topStoryDao.getAllStories() }
                cancelAndIgnoreRemainingEvents()
            }
        }
}