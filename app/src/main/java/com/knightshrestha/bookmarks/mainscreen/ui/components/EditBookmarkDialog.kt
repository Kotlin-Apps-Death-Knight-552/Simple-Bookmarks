package com.knightshrestha.bookmarks.mainscreen.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.knightshrestha.bookmarks.mainscreen.events.BookmarkEvent
import com.knightshrestha.bookmarks.mainscreen.models.Bookmark

@Composable
fun EditBookmarkDialog(
    bookmark: Bookmark,
    onEvent: (BookmarkEvent) -> Unit,
    toggleDialog: () -> Unit
) {
    var name by remember { mutableStateOf(bookmark.name) }
    var link by remember { mutableStateOf(bookmark.link) }

    AlertDialog(onDismissRequest = {
        toggleDialog()
    }, confirmButton = {
        TextButton(onClick = {
            onEvent(
                BookmarkEvent.UpdateBookmark(
                    bookmark.copy(
                        name = name,
                        link = link
                    )
                )
            )
            toggleDialog()
        }) {
            Text(text = "Save")
        }
    }, title = {
        Text(text = "Update")
    }, text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value = name, onValueChange = {
                name = it
            },
                label = { Text(text = "Name") })
            OutlinedTextField(value = link, onValueChange = {
                link = it
            },
                label = { Text(text = "Link") })

        }
    })

}