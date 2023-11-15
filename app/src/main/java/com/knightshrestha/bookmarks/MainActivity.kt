package com.knightshrestha.bookmarks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.knightshrestha.bookmarks.core.helpers.UiState
import com.knightshrestha.bookmarks.loading.ui.screen.LoadingScreen
import com.knightshrestha.bookmarks.authentication.ui.screen.LoginScreen
import com.knightshrestha.bookmarks.mainscreen.ui.screen.MainScreen
import com.knightshrestha.bookmarks.ui.theme.BookmarksTheme
import com.knightshrestha.bookmarks.authentication.viewmodel.MainViewModel
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
