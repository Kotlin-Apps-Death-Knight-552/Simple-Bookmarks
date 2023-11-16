package com.knightshrestha.bookmarks.mainscreen.repository

import com.knightshrestha.bookmarks.mainscreen.models.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarks(): Flow<List<Bookmark>>

    suspend fun insertBookmark(name: String, link: String): Pair<Boolean, String?>

    suspend fun insertBookmarks(bookmarks: List<Bookmark>)
    suspend fun updateBookmark(bookmark: Bookmark): Pair<Boolean, String?>
    suspend fun deleteBookmark(bookmark: Bookmark): Pair<Boolean, String?>
}