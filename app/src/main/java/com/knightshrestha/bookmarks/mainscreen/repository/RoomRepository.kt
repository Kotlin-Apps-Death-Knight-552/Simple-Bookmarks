package com.knightshrestha.bookmarks.mainscreen.repository

import com.knightshrestha.bookmarks.mainscreen.models.Bookmark
import com.knightshrestha.bookmarks.mainscreen.models.RoomDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor(private val dao: RoomDAO): BookmarkRepository {
    override fun getBookmarks(): Flow<List<Bookmark>> {
        return dao.getBookmarkList()
    }

    override suspend fun insertBookmark(name: String, link: String): Pair<Boolean, String?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertBookmarks(bookmarks: List<Bookmark>) {
        dao.upsertBookmarks(bookmarks)
    }

    override suspend fun updateBookmark(bookmark: Bookmark): Pair<Boolean, String?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBookmark(bookmark: Bookmark): Pair<Boolean, String?> {
        TODO("Not yet implemented")
    }
}