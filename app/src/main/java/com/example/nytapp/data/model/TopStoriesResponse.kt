package com.example.nytapp.data.model

data class TopStoriesResponse(
    val results: List<TopStoryResponseData>
)

data class TopStoryResponseData(
    val title: String,
    val abstract: String,
    val url: String,
    val byline: String,
    val multimedia: List<MultimediaData>
)

data class MultimediaData(
    val url: String
)