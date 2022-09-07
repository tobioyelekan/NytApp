package com.example.nytapp.features.top_story_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.nytapp.helpers.NavArgs
import com.example.nytapp.repositories.TopStoryDetailsRepository
import com.example.nytapp.MainCoroutineRule
import com.example.nytapp.SampleTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopStoryDetailsViewModelTest {
    @get:Rule
    var instantTaskExecutorRue = InstantTaskExecutorRule()

    @get:Rule
    var mainTestDispatcher = MainCoroutineRule()

    private lateinit var viewModel: TopStoryDetailViewModel
    private val repository = mockk<TopStoryDetailsRepository>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    @Before
    fun setup() {
        coEvery { repository.getTopStoryData(any()) } returns SampleTestData.sampleStoryData[0]
        every { savedStateHandle.get<String>(NavArgs.storyId) } returns "storyId"
        viewModel = TopStoryDetailViewModel(repository, savedStateHandle)
    }

    private fun setupWithNullResponse() {
        coEvery { repository.getTopStoryData(any()) } returns null
        every { savedStateHandle.get<String>(NavArgs.storyId) } returns "storyId"
        viewModel = TopStoryDetailViewModel(repository, savedStateHandle)
    }

    @Test
    fun `assert that at laucnh of viewModel, loading is emitted and when done loading is set to false`() =
        runBlocking {
            mainTestDispatcher.pauseDispatcher()

            setup()

            viewModel.viewState.test {
                val viewState = awaitItem()
                assertEquals(viewState.isLoading, true)
            }

            mainTestDispatcher.resumeDispatcher()

            viewModel.viewState.test {
                val viewState = awaitItem()
                assertEquals(viewState.isLoading, false)
            }
        }

    @Test
    fun `assert that when a storyId is passed, top data is returned if data exists`() =
        runBlocking {
            viewModel.viewState.test {
                val viewState = awaitItem()
                assertEquals(viewState.isLoading, false)
                assertNotNull(viewState.storyDetails)
                assertEquals(viewState.storyDetails?.id, SampleTestData.sampleStoryData[0].id)
                assertEquals(viewState.storyDetails?.title, SampleTestData.sampleStoryData[0].title)
                coVerify { repository.getTopStoryData("storyId") }
            }
        }

    @Test
    fun `assert that when a storyId is passed, and data is not found top data is not returned`() =
        runBlocking {
            setupWithNullResponse()
            viewModel.viewState.test {
                val viewState = awaitItem()
                assertEquals(viewState.isLoading, false)
                assertNull(viewState.storyDetails)
                coVerify { repository.getTopStoryData("storyId") }
            }
        }
}