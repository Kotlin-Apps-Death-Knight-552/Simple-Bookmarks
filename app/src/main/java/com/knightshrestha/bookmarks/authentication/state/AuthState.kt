package com.knightshrestha.bookmarks.authentication.state

import com.knightshrestha.bookmarks.core.helpers.UiState

data class AuthState(
    val uiState: UiState = UiState.LOADING,
)