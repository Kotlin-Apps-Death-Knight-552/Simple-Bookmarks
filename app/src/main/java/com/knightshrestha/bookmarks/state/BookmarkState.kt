package com.knightshrestha.bookmarks.state

import com.knightshrestha.bookmarks.enums.UiState
import com.knightshrestha.bookmarks.graphql.LiveBookmarkListSubscription

data class BookmarkState(
    val bookmarks: List<LiveBookmarkListSubscription.Bookmark> = emptyList(),
    val uiState: UiState = UiState.LOADING,
    val token: String? = ""
)
