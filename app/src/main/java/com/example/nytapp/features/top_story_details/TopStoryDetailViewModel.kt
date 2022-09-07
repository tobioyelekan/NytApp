package com.example.nytapp.features.top_story_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.helpers.NavArgs
import com.example.nytapp.repositories.TopStoryDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopStoryDetailViewModel @Inject constructor(
    private val repo: TopStoryDetailsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var viewState = MutableStateFlow(ViewState())
        private set
    private val storyId = requireNotNull(savedStateHandle.get<String>(NavArgs.storyId))

    init {
        fetchStory()
    }

    private fun fetchStory() {
        viewState.value = viewState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = repo.getTopStoryData(storyId)
            viewState.value = viewState.value.copy(isLoading = false, storyDetails = result)
        }
    }

    data class ViewState(
        var isLoading: Boolean = false,
        var storyDetails: TopStoryData? = null
    )
}