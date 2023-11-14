package com.knightshrestha.bookmarks.state

import com.knightshrestha.bookmarks.helpers.UiState

data class AuthState(
    val uiState: UiState = UiState.LOADING,
)