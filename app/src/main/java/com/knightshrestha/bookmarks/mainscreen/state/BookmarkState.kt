package com.knightshrestha.bookmarks.mainscreen.state

import com.knightshrestha.bookmarks.core.helpers.SortType
import com.knightshrestha.bookmarks.graphql.LiveBookmarkListOrderedByNameSubscription

data class BookmarkState(
    val bookmarks: List<LiveBookmarkListOrderedByNameSubscription.Bookmark> = emptyList(),

    val token: String? = "",
    val sortType: SortType = SortType.NAME
)
