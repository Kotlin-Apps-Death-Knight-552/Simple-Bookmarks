package com.knightshrestha.bookmarks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knightshrestha.bookmarks.enums.UiState
import com.knightshrestha.bookmarks.graphql.LiveBookmarkListSubscription
import com.knightshrestha.bookmarks.repository.AuthRepository
import com.knightshrestha.bookmarks.repository.DataStoreRepository
import com.knightshrestha.bookmarks.state.BookmarkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository

): ViewModel() {
    private val _token = dataStoreRepository.getAccessToken()
    private val _uiState = MutableStateFlow(UiState.LOADING)
    private val _bookmarkState = MutableStateFlow(BookmarkState())
    private val _bookmarkList =
        MutableStateFlow<List<LiveBookmarkListSubscription.Bookmark>>(
            emptyList()
        )

    suspend fun loginToken(): String? {
        return ""
    }


    val state = combine(_uiState, _bookmarkState, _bookmarkList, _token ) {uiState, bookmarkState, bookmarks, token ->
        bookmarkState.copy(
            bookmarks = bookmarks,
            uiState = uiState,
            token = token
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), BookmarkState())

    fun logout() {
        viewModelScope.launch {  }
        _uiState.update {
            UiState.LOGGED_OUT
        }
    }

    fun getToken(): Flow<String?> {
        return dataStoreRepository.getAccessToken()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).let {
                when (it.isSuccessful) {
                    true -> {
                        dataStoreRepository.saveAccessToken(it.body()!!.session.accessToken)
                        _uiState.update {
                            UiState.LOGGED_IN
                        }
                    }
                    false -> {
                        dataStoreRepository.deleteAccessToken()
                        _uiState.update {
                            UiState.LOGGED_OUT
                        }
                    }
                }
            }

        }

    }

    fun changeUiState(state: UiState) {
        _uiState.update {
            state
        }
    }

}