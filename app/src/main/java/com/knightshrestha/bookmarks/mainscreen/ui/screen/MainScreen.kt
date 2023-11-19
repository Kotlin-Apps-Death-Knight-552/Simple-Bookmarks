package com.knightshrestha.bookmarks.mainscreen.ui.screen

import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.knightshrestha.bookmarks.mainscreen.events.BookmarkEvent
import com.knightshrestha.bookmarks.mainscreen.ui.components.AddBookmarkDialog
import com.knightshrestha.bookmarks.mainscreen.ui.components.ListItem
import com.knightshrestha.bookmarks.mainscreen.ui.components.MainAppBar
import com.knightshrestha.bookmarks.mainscreen.ui.components.StickyHeader
import com.knightshrestha.bookmarks.mainscreen.viewmodel.BookmarkViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {


    val viewModel = hiltViewModel<BookmarkViewModel>()
    val onEvent = viewModel::onBookmarkEvent
    val state by viewModel.state.collectAsState()

    //Screen Snack
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.snackMessage) {
        if (state.snackMessage.isBlank().not()) {
            snackbarHostState.showSnackbar(
                message = state.snackMessage,
                duration = SnackbarDuration.Short
            )

        }
    }


    //End
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(BookmarkEvent.ToggleSaveBookmark)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "ADD")
            }
        },
        topBar = {
            MainAppBar(onEvent = onEvent, state = state)
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.isInserting) {
                AddBookmarkDialog(state = state, onEvent = onEvent)
            }

            var currentItem by remember { mutableStateOf("") }

            val ctx = LocalContext.current
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.bookmarks.forEach {
                    stickyHeader {
                        StickyHeader(label = it.label)
                    }
                    items(it.items) { bookmark ->
                        ListItem(
                            state = state,
                            bookmark = bookmark,
                            onEvent = onEvent,
                            onClick = {
                                if (URLUtil.isValidUrl(bookmark.link)) {
                                    val intent =
                                        Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.link))
                                    ctx.startActivity(intent)
                                }


                            },
                            onLongClick = {
                                currentItem = bookmark.id
                            },
                            hasExpanded = currentItem == bookmark.id
                        )
                    }
                }

            }

        }


    }

}