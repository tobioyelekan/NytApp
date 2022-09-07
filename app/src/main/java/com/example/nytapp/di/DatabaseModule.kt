package com.example.nytapp.di

import android.content.Context
import com.example.nytapp.data.local.TopStoryDatabase
import com.example.nytapp.data.local.TopStoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): TopStoryDatabase {
        return TopStoryDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun providesRateDao(database: TopStoryDatabase): TopStoryDao {
        return database.storyDao()
    }
}