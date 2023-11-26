package com.knightshrestha.bookmarks.mainscreen.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.knightshrestha.bookmarks.core.helpers.SortType
import com.knightshrestha.bookmarks.mainscreen.events.BookmarkEvent
import com.knightshrestha.bookmarks.mainscreen.state.BookmarkState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(onEvent: (BookmarkEvent) -> Unit, state: BookmarkState, logOut: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = "Bookmark List "
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ), actions = {
            IconButton(onClick = {
                menuExpanded = !menuExpanded
            }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = {
                menuExpanded = false
            }) {
                SortType.values().map { sort ->
                    DropdownMenuItem(text = {
                        Row(modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                onEvent(BookmarkEvent.SortBookmarks(sort))
                            }, verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = sort == state.sortedBy,
                                onClick = { onEvent(BookmarkEvent.SortBookmarks(sort)) })
                            Text(text = sort.name)
                        }
                    }, onClick = { onEvent(BookmarkEvent.SortBookmarks(sort)) })

                }
                
                DropdownMenuItem(text = { Text(text = "Log Out") }, onClick = { logOut() })
            }
        }
    )



}