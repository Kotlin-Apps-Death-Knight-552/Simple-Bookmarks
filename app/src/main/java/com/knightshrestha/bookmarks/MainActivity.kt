package com.knightshrestha.bookmarks

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.knightshrestha.bookmarks.authentication.ui.screen.LoginScreen
import com.knightshrestha.bookmarks.authentication.viewmodel.MainViewModel
import com.knightshrestha.bookmarks.core.helpers.UiState
import com.knightshrestha.bookmarks.loading.ui.screen.LoadingScreen
import com.knightshrestha.bookmarks.mainscreen.events.BookmarkEvent
import com.knightshrestha.bookmarks.mainscreen.ui.screen.MainScreen
import com.knightshrestha.bookmarks.mainscreen.viewmodel.BookmarkViewModel
import com.knightshrestha.bookmarks.ui.theme.BookmarksTheme
import dagger.hilt.android.AndroidEntryPoint
import com.supersuman.apkupdater.ApkUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        handleIntent(intent = intent)
        checkForUpdates()


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
                        MainScreen(logOut = { viewModel.logout() })
                    }
                    UiState.LOGGED_OUT -> {
                        LoginScreen(viewModel)
                    }
                }
                
            }
        }
    }

    private fun checkForUpdates() = CoroutineScope(Dispatchers.IO).launch {
        val url = "https://github.com/Kotlin-Apps-Death-Knight-552/Simple-Bookmarks/releases/latest"
        val updater = ApkUpdater(this@MainActivity, url)
        
        updater.threeNumbers = false
        if (updater.isInternetConnection()) {
            updater.init()
            updater.isNewUpdateAvailable {
                if (updater.hasPermissionsGranted()) {
                    updater.requestDownload()
                } else {
                    updater.requestMyPermissions {
                        updater.requestDownload()
                    }
                }
            }
        }
    }

//    private fun handleIntent(intent: Intent) {
//        val receivedAction = intent.action
//        val receivedType = intent.type
//
//        if (receivedAction == Intent.ACTION_SEND && receivedType != null) {
//            if (receivedType == "text/plain") {
//                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
//
//                if (sharedText != null) {
//                    val onEvent = bookmarkViewModel::onBookmarkEvent
//                    onEvent(BookmarkEvent.SetLink(sharedText))
//                    onEvent(BookmarkEvent.ToggleSaveBookmark)
//                }
//            }
//        }
//
//    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        if (intent != null) {
//            handleIntent(intent = intent)
//        }
//    }
}
