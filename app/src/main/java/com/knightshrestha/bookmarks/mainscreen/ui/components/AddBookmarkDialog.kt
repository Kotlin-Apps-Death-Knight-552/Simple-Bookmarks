package com.knightshrestha.bookmarks.mainscreen.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.knightshrestha.bookmarks.mainscreen.events.BookmarkEvent
import com.knightshrestha.bookmarks.mainscreen.state.BookmarkState

@Composable
fun AddBookmarkDialog(state: BookmarkState, onEvent: (BookmarkEvent) -> Unit) {
    AlertDialog(onDismissRequest = {
        onEvent(BookmarkEvent.HideDialog)
    }, confirmButton = {
                       TextButton(onClick = {
                           onEvent(BookmarkEvent.InsertBookmark)
                       }) {
                           Text(text = "Save")
                       }
                       }, title = {
        Text(text = "Upsert")
    }, text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value = state.name, onValueChange = {
                onEvent(BookmarkEvent.SetName(it))
            },
                label = { Text(text = "Name") })
            OutlinedTextField(value = state.link, onValueChange = {
                onEvent(BookmarkEvent.SetLink(it))
            },
                label = { Text(text = "Link") })

        }
    })

}