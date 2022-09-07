package com.example.nytapp.features.all_top_stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.helpers.Resource
import com.example.nytapp.repositories.TopStoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllStoriesViewModel @Inject constructor(
    private val repository: TopStoryRepository
) : ViewModel() {
    private var state = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = state

    val eventActions = MutableSharedFlow<EventActions>()

    init {
        viewModelScope.launch {
            repository.collectStoryData().collect {
                state.value = state.value.copy(topStories = it)
            }
        }
        fetchTopStories()
    }

    fun refresh() {
        if (!viewState.value.isLoading) {
            fetchTopStories()
        }
    }

    private fun fetchTopStories() {
        state.value = state.value.copy(isLoading = true)

        viewModelScope.launch {
            val result = repository.fetchTopStories()
            state.value = state.value.copy(isLoading = false)
            when (result) {
                is Resource.Error -> {
                    eventActions.emit(
                        EventActions.ShowMessage(
                            result.message ?: "something went wrong"
                        )
                    )
                }
                else -> {}
            }
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val topStories: List<TopStoryData> = emptyList()
    )

    sealed class EventActions {
        class ShowMessage(val msg: String) : EventActions()
    }
}