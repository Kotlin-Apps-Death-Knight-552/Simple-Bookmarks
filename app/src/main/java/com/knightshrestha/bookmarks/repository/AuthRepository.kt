package com.knightshrestha.bookmarks.repository

import com.knightshrestha.bookmarks.models.LoginResponse
import com.knightshrestha.bookmarks.services.AuthApiService
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService){
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return authApiService.login(
            AuthApiService.LoginObject(
                email, password
            )
        )
    }
}