package com.knightshrestha.bookmarks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.knightshrestha.bookmarks.enums.UiState
import com.knightshrestha.bookmarks.repository.AuthRepository
import com.knightshrestha.bookmarks.repository.BookmarkRepository
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
    private val bookmarkRepository: BookmarkRepository,
    private val dataStoreRepository: DataStoreRepository

) : ViewModel() {
    private val _token = dataStoreRepository.getAccessToken()
    private val _uiState = MutableStateFlow(UiState.LOADING)
    private val _bookmarkState = MutableStateFlow(BookmarkState())

    val state = combine(
        _uiState,
        _bookmarkState,
                _token
    ) { uiState, bookmarkState, token ->
        bookmarkState.copy(
            uiState = uiState,
            token = token
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), BookmarkState())

    fun logout() {
        viewModelScope.launch {
            dataStoreRepository.deleteAccessToken()
            dataStoreRepository.deleteRefreshToken()
            dataStoreRepository.deleteUserID()

            _uiState.update {
                UiState.LOGGED_OUT
            }
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
                        dataStoreRepository.saveRefreshToken(it.body()!!.session.refreshToken)

                        dataStoreRepository.saveUserID(
                            JWT(
                                it.body()!!.session.accessToken
                            ).getClaim("sub").asString()!!
                        )

                        _uiState.update {
                            UiState.LOGGED_IN
                        }
                    }

                    false -> {
                        dataStoreRepository.deleteAccessToken()
                        dataStoreRepository.deleteRefreshToken()
                        dataStoreRepository.deleteUserID()
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