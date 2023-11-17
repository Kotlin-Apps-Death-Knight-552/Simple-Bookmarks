package com.knightshrestha.bookmarks.mainscreen.state

import com.knightshrestha.bookmarks.core.helpers.SortType
import com.knightshrestha.bookmarks.mainscreen.models.Bookmark


data class GroupedBookmarks(
    val label: String,
    val items: List<Bookmark>
)

data class BookmarkState(
    val bookmarks: List<GroupedBookmarks> = emptyList(),
    val name: String = "",
    val link: String = "",
    val isInserting: Boolean = false,
    val sortedBy: SortType = SortType.TIME,
    val snackMessage: String = ""
)