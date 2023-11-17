package com.knightshrestha.bookmarks.mainscreen.di

import android.content.Context
import androidx.room.Room
import com.knightshrestha.bookmarks.mainscreen.models.MainDatabase
import com.knightshrestha.bookmarks.mainscreen.models.RoomDAO
import com.knightshrestha.bookmarks.mainscreen.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDI {
    @Provides
    fun provideDAO(mainDatabase: MainDatabase): RoomDAO {
        return mainDatabase.roomDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context): MainDatabase {
        return Room.databaseBuilder(
            applicationContext,
            MainDatabase::class.java,
            "Origin"
        ).build()
    }

    @Provides
    fun provideRoomRepo(dao: RoomDAO): RoomRepository {
        return RoomRepository(dao)
    }
}