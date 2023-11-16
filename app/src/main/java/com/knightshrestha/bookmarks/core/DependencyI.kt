package com.knightshrestha.bookmarks.core

import android.content.Context
import com.knightshrestha.bookmarks.core.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DependencyI {
    @Provides
    fun provideContext(@ApplicationContext a: Context): Context {
        return a
    }

    @Provides
    fun provideDataStore(context: Context): DataStoreRepository {
        return DataStoreRepository(context)
    }
}