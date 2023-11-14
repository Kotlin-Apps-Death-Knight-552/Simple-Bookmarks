package com.knightshrestha.bookmarks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.knightshrestha.bookmarks.helpers.UiState
import com.knightshrestha.bookmarks.ui.screen.LoadingScreen
import com.knightshrestha.bookmarks.ui.screen.LoginScreen
import com.knightshrestha.bookmarks.ui.screen.MainScreen
import com.knightshrestha.bookmarks.ui.theme.BookmarksTheme
import com.knightshrestha.bookmarks.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            val state by viewModel.state.collectAsState()
            BookmarksTheme {
                // A surface container using the 'background' color from the theme
                
                when (state.uiState) {
                    UiState.LOADING -> {
                        LoadingScreen(viewModel)
                    }
                    UiState.LOGGED_IN -> {
                        MainScreen(viewModel)
                    }
                    UiState.LOGGED_OUT -> {
                        LoginScreen(viewModel)
                    }
                }
                
            }
        }
    }
}
