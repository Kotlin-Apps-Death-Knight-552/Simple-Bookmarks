package com.knightshrestha.bookmarks.authentication.services

import com.knightshrestha.bookmarks.authentication.models.LoginResponse
import com.knightshrestha.bookmarks.authentication.models.Session
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    data class LoginObject(
        val email: String,
        val password: String
    )
    @POST("signin/email-password")
    suspend fun login(@Body loginObject: LoginObject): Response<LoginResponse>

    data class RefreshTokenObject(
        val refreshToken: String
    )
    @POST("token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenObject): Response<Session>

}