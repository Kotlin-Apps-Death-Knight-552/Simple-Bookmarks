package com.knightshrestha.bookmarks.mainscreen.models

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDAO {

    @Query("SELECT * FROM bookmark_list")
    fun getBookmarkList(): Flow<List<Bookmark>>

    @Upsert
    suspend fun upsertBookmarks(bookmarks: List<Bookmark>): List<Long>
}