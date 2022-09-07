package com.example.nytapp

import com.example.nytapp.data.model.MultimediaData
import com.example.nytapp.data.model.TopStoriesResponse
import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.data.model.TopStoryResponseData

object SampleTestData {
    val sampleStoryResponse = TopStoriesResponse(
        results = listOf(
            TopStoryResponseData(
                title = "title",
                abstract = "summary",
                url = "moreContentUrl",
                byline = "author",
                multimedia = listOf(MultimediaData("imgUrl"))
            )
        )
    )
    val sampleStoryData = listOf(
        TopStoryData(
            id = "1",
            title = "title1",
            author = "author1",
            summary = "summary1",
            imgUrl = "imgUrl1",
            moreContentUrl = "moreContentUrl1"
        ),
        TopStoryData(
            id = "2",
            title = "title2",
            author = "author2",
            summary = "summary2",
            imgUrl = "imgUrl2",
            moreContentUrl = "moreContentUrl2"
        ),
        TopStoryData(
            id = "3",
            title = "title3",
            author = "author3",
            summary = "summary3",
            imgUrl = "imgUrl3",
            moreContentUrl = "moreContentUrl3"
        )
    )
}