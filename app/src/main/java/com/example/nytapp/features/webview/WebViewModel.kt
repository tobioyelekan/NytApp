package com.example.nytapp.features.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nytapp.helpers.NavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val storyUrl = requireNotNull(savedStateHandle.get<String>(NavArgs.storyUrl))
    var url = MutableStateFlow(storyUrl)
        private set
}