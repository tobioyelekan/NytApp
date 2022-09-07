package com.example.nytapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.nytapp.data.local.TopStoryDao
import com.example.nytapp.data.local.TopStoryDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class DbSetup {
    @get:Rule
    var mainTestDispatcher = MainCoroutineRule()

    private lateinit var db: TopStoryDatabase
    protected lateinit var topStoryDao: TopStoryDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TopStoryDatabase::class.java
        ).build()

        topStoryDao = db.storyDao()
    }

    @After
    fun closeDb() {
        db.close()
    }
}