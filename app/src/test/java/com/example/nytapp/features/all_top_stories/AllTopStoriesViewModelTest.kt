package com.example.nytapp.features.all_top_stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.helpers.Resource
import com.example.nytapp.repositories.TopStoryRepository
import com.example.nytapp.MainCoroutineRule
import com.example.nytapp.SampleTestData
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.pauseDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AllTopStoriesViewModelTest {
    @get:Rule
    var instantTaskExecutorRue = InstantTaskExecutorRule()

    @get:Rule
    var mainTestDispatcher = MainCoroutineRule()

    private lateinit var viewModel: AllStoriesViewModel
    private val repository = mockk<TopStoryRepository>()

    @Before
    fun setup() {
        coEvery { repository.fetchTopStories() } returns Resource.Success(Unit)
        coEvery { repository.collectStoryData() } returns flowOf(SampleTestData.sampleStoryData)
        viewModel = AllStoriesViewModel(repository)
    }

    private fun setupWithEmptyNetworkAndCachedResult() {
        coEvery { repository.fetchTopStories() } returns Resource.Success(Unit)
        coEvery { repository.collectStoryData() } returns flowOf(emptyList())
        viewModel = AllStoriesViewModel(repository)
    }

    private fun setupWithErrorAndReturnCachedData() {
        coEvery { repository.fetchTopStories() } returns Resource.Error("something went wrong")
        coEvery { repository.collectStoryData() } returns flowOf(SampleTestData.sampleStoryData)
        viewModel = AllStoriesViewModel(repository)
    }

    @Test
    fun `verify that isLoading is set to true when viewModel is launched and isLoading is set to false after getting response from repository`() =
        runBlocking {
            mainTestDispatcher.pauseDispatcher()

            //reset viewModel
            setup()

            viewModel.viewState.test {
                assertEquals(awaitItem().isLoading, true)
            }

            mainTestDispatcher.dispatcher.resumeDispatcher()

            viewModel.viewState.test {
                val viewState = awaitItem()
                assertEquals(viewState.isLoading, false)
                assertEquals(viewState.topStories, SampleTestData.sampleStoryData)
            }

            coVerify { repository.fetchTopStories() }
        }

    @Test
    fun `verify that at launch of viewModel data is fetched when repository call to remote source is successful`() =
        runBlocking {
            coVerify { repository.fetchTopStories() }
            coVerify { repository.collectStoryData() }

            viewModel.viewState.test {
                val viewState = awaitItem()
                assertEquals(viewState.isLoading, false)
                assertEquals(viewState.topStories, SampleTestData.sampleStoryData)
            }
        }

    @Test
    fun `verify that at launch of viewModel, when repository call to remote data source is not successful and there's cached data, assert that cached data is returned`() {
        runBlocking {
            launch {
                viewModel.viewState.test {
                    val viewState = awaitItem()
                    assertEquals(viewState.isLoading, false)
                    assertEquals(viewState.topStories, SampleTestData.sampleStoryData)
                }

                viewModel.eventActions.test {
                    setupWithErrorAndReturnCachedData()
                    coVerify { repository.fetchTopStories() }
                    coVerify { repository.collectStoryData() }
                    assertEquals(
                        awaitItem(),
                        AllStoriesViewModel.EventActions.ShowMessage("something went wrong")
                    )
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    @Test
    fun `verify that at launch of viewModel, when network call is successful, and it returns empty data and cache is empty, assert that cache data returns empty list`() =
        runBlocking {
            setupWithEmptyNetworkAndCachedResult()
            coVerify { repository.fetchTopStories() }
            coVerify { repository.collectStoryData() }

            viewModel.viewState.test {
                val viewState = awaitItem()
                assertEquals(viewState.isLoading, false)
                assertEquals(viewState.topStories, emptyList<TopStoryData>())
            }
        }

    @Test
    fun `assert that when refresh() is called and the view state is currently not loading, remote data is fetched again`() =
        runBlocking {
            mainTestDispatcher.pauseDispatcher()

            viewModel.refresh()

            viewModel.viewState.test {
                assertEquals(awaitItem().isLoading, true)
            }

            mainTestDispatcher.dispatcher.resumeDispatcher()

            viewModel.viewState.test {
                val viewState = awaitItem()
                assertEquals(viewState.isLoading, false)
                assertEquals(viewState.topStories, SampleTestData.sampleStoryData)
            }

            coVerify { repository.fetchTopStories() }
        }

//    @Test
//    fun `assert that when refresh() is called and the view state is currently loading, remote data is only called once`() =
//        runBlocking {
//            mainTestDispatcher.pauseDispatcher()
//
//            setup()
//
//            viewModel.refresh()
//
//            coVerify(exactly = 1) { repository.fetchTopStories() }
//
//            viewModel.viewState.test {
//                assertEquals(awaitItem().isLoading, true)
//            }
//
//            mainTestDispatcher.dispatcher.resumeDispatcher()
//
//            viewModel.viewState.test {
//                val viewState = awaitItem()
//                assertEquals(viewState.isLoading, false)
//                assertEquals(viewState.topStories, SampleTestData.sampleStoryData)
//            }
//
//        }
}
