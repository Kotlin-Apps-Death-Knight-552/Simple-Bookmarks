package com.knightshrestha.bookmarks.mainscreen.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.knightshrestha.bookmarks.R
import com.knightshrestha.bookmarks.core.helpers.parseAndFormatTimestamp
import com.knightshrestha.bookmarks.mainscreen.events.BookmarkEvent
import com.knightshrestha.bookmarks.mainscreen.models.Bookmark
import com.knightshrestha.bookmarks.mainscreen.state.BookmarkState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(state: BookmarkState, bookmark: Bookmark, onEvent: (BookmarkEvent) -> Unit, onClick: () -> Unit, onLongClick: () -> Unit, hasExpanded: Boolean = false) {
    var openDeleteDialog by remember { mutableStateOf(false) }
    var openEditDialog by remember { mutableStateOf(false) }


    if (openDeleteDialog) {
        DeleteBookmarkDialog(bookmark = bookmark, onEvent = onEvent, toggleDialog = {
            openDeleteDialog = !openDeleteDialog
        })
    }

    if (openEditDialog) {
        EditBookmarkDialog(bookmark = bookmark, onEvent = onEvent, toggleDialog = {
            openEditDialog = !openEditDialog
        })
    }

    OutlinedCard(modifier = Modifier
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
        .padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.rich_folder),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "⌛${parseAndFormatTimestamp(bookmark.time)}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(text = bookmark.name, style = MaterialTheme.typography.titleMedium)
            }
        }
        AnimatedVisibility(visible = hasExpanded) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = {
                    openEditDialog = true
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    Text(text = "Edit")
                }
                TextButton(onClick = {
                    openDeleteDialog = true

                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    Text(text = "Delete")
                }
            }
        }
    }
}
