package com.example.nytapp.data.model

import androidx.room.Entity
import java.util.*

@Entity(tableName = "top_story_table", primaryKeys = ["title", "author"])
data class TopStoryData(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val summary: String,
    val imgUrl: String,
    val moreContentUrl: String
)