package com.example.nytapp.features.webview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.nytapp.helpers.NavArgs
import com.example.nytapp.MainCoroutineRule
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WebViewModelTest {
    @get:Rule
    var instantTaskExecutorRue = InstantTaskExecutorRule()

    @get:Rule
    var mainTestDispatcher = MainCoroutineRule()

    private lateinit var viewModel: WebViewModel
    private val savedStateHandle = mockk<SavedStateHandle>()

    @Before
    fun setup() {
        every { savedStateHandle.get<String>(NavArgs.storyUrl) } returns "storyUrl"
        viewModel = WebViewModel(savedStateHandle)
    }

    @Test
    fun `verify that on launch of viewModel, passed data is emitted gotten from savedStateHandle`() =
        runBlocking {
            viewModel.url.test {
                val url = awaitItem()
                assertNotNull(url)
                assertEquals(url, "storyUrl")
            }
        }
}