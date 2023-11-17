package com.knightshrestha.bookmarks.mainscreen.events

import com.knightshrestha.bookmarks.core.helpers.SortType
import com.knightshrestha.bookmarks.mainscreen.models.Bookmark

sealed interface BookmarkEvent {
    data class SetName(val name: String) : BookmarkEvent
    data class SetLink(val link: String): BookmarkEvent

    data object InsertBookmark: BookmarkEvent
    data class DeleteBookmark(val bookmark: Bookmark): BookmarkEvent
    data class UpdateBookmark(val bookmark: Bookmark): BookmarkEvent

    data object ToggleSaveBookmark: BookmarkEvent

    data object HideDialog: BookmarkEvent
    data class SortBookmarks(val sortBy: SortType): BookmarkEvent

}