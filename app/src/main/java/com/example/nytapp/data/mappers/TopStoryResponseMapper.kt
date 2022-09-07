package com.example.nytapp.data.mappers

import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.data.model.TopStoryResponseData
import javax.inject.Inject

class TopStoryResponseMapper @Inject constructor() {
    operator fun invoke(response: TopStoryResponseData): TopStoryData {
        return TopStoryData(
            title = response.title,
            author = response.byline,
            summary = response.abstract,
            imgUrl = response.multimedia[0].url,
            moreContentUrl = response.url
        )
    }
}