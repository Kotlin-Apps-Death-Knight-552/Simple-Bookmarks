package com.knightshrestha.bookmarks.mainscreen.events

import com.knightshrestha.bookmarks.mainscreen.models.Bookmark

sealed interface BookmarkEvent {
    data class SetName(val name: String) : BookmarkEvent
    data class SetLink(val link: String): BookmarkEvent

    data class UpsertBookmark(val bookmark: Bookmark): BookmarkEvent
    data class RemoveBookmark(val bookmark: Bookmark): BookmarkEvent

    data object SaveBookmark: BookmarkEvent
    data object DeleteBookmark: BookmarkEvent

}