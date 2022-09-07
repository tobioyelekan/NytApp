package com.example.nytapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.example.nytapp.data.model.TopStoryData
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TopStoryDaoTest : DbSetup() {

    @Test
    fun insertAndReadTopStories() = runBlocking {
        saveAllStories()

        val stories = topStoryDao.getAllStories()
        stories.test {
            assertEquals(awaitItem(), SampleTestData.sampleStoryData)
        }
    }

    @Test
    fun insertAndGetTopStoryById() = runBlocking {
        saveAllStories()

        val story = topStoryDao.getStoryById(SampleTestData.sampleStoryData[0].id)
        assertNotNull(story)
        assertEquals(story, SampleTestData.sampleStoryData[0])
    }

    @Test
    fun insertAndGetNonExistentTopStoryById() = runBlocking {
        saveAllStories()

        val story = topStoryDao.getStoryById("id")
        assertNull(story)
    }

    @Test
    fun insertDuplicateId() = runBlocking {
        val story1 = TopStoryData(id = "1", "title", "author", "summary", "url", "url")
        val story2 = TopStoryData(id = "1", "title", "author", "summary", "url", "url")
        val story3 = TopStoryData(id = "2", "title2", "author2", "summary2", "url2", "url2")

        topStoryDao.saveStories(listOf(story1, story2, story3))

        val results = topStoryDao.getAllStories()
        results.test {
            val items = awaitItem()
            assertEquals(items.size, 2)
            assertEquals(items.first(), story2)
            assertEquals(items.last(), story3)
        }
    }

    private fun saveAllStories() {
        runBlocking {
            val stories = SampleTestData.sampleStoryData
            topStoryDao.saveStories(stories)
        }
    }
}