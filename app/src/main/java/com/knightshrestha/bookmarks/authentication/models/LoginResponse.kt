package com.knightshrestha.bookmarks.authentication.models

data class LoginResponse(
    val session: Session,
    val mfa: String? = null
)

data class Session(
    val accessToken: String,
    val refreshToken: String
)

data class ErrorResponse(
    val status: Int,
    val message: String,
    val error: String
)