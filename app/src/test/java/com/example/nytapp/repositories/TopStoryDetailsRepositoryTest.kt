package com.example.nytapp.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nytapp.data.local.LocalDataSource
import com.example.nytapp.MainCoroutineRule
import com.example.nytapp.SampleTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopStoryDetailsRepositoryTest {
    @get:Rule
    var instantTaskExecutorRue = InstantTaskExecutorRule()

    @get:Rule
    var mainTestDispatcher = MainCoroutineRule()

    private lateinit var repository: TopStoryDetailsRepository
    private val localDataSource = mockk<LocalDataSource>()

    @Before
    fun setup() {
        repository = TopStoryDetailsRepository(localDataSource)
    }

    @Test
    fun `verify that when an id exist in the local data, top story data is returned`() =
        runBlocking {
            coEvery { localDataSource.getStoryDataById("id") } returns SampleTestData.sampleStoryData[0]

            val result = repository.getTopStoryData("id")

            assertNotNull(result)
            assertEquals(result, SampleTestData.sampleStoryData[0])
            coVerify { repository.getTopStoryData("id") }
        }

    @Test
    fun `verify that when an id does not exist in the local data, null data is returned`() =
        runBlocking {
            coEvery { localDataSource.getStoryDataById("id") } returns null

            val result = repository.getTopStoryData("id")

            assertNull(result)
            coVerify { repository.getTopStoryData("id") }
        }

}