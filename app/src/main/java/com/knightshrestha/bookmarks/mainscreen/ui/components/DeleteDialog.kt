package com.knightshrestha.bookmarks.mainscreen.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.knightshrestha.bookmarks.mainscreen.events.BookmarkEvent
import com.knightshrestha.bookmarks.mainscreen.models.Bookmark

@Composable
fun DeleteBookmarkDialog(
    bookmark: Bookmark,
    onEvent: (BookmarkEvent) -> Unit,
    toggleDialog: () -> Unit
) {

    AlertDialog(onDismissRequest = {
        toggleDialog()
    }, confirmButton = {
        TextButton(onClick = {
            onEvent(BookmarkEvent.DeleteBookmark(bookmark))
            toggleDialog()
        }) {
            Text(text = "Yes")
        }

    }, dismissButton = {

        TextButton(onClick = {
            toggleDialog()
        }) {
            Text(text = "No")
        }
    }, title = { Text(text = "Delete") },
        text = {
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = bookmark.name)
                Text(text = bookmark.link)
            }
        })
}