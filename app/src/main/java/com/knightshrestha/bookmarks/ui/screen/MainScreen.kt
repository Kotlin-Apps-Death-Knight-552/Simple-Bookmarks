package com.knightshrestha.bookmarks.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.knightshrestha.bookmarks.viewmodel.BookmarkViewModel
import com.knightshrestha.bookmarks.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val someViewModel = hiltViewModel<BookmarkViewModel>()
    val state by someViewModel.state.collectAsState()


    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {


                items(state.bookmarks) {bookmark ->
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            Text(text = bookmark.name)

                        }
                    }
                }
            }

        }


    }

}