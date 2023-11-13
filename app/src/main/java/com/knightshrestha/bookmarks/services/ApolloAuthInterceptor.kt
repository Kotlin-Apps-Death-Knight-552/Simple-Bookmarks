package com.knightshrestha.bookmarks.services

import android.util.Log
import com.auth0.android.jwt.JWT
import com.knightshrestha.bookmarks.AUTH_URL
import com.knightshrestha.bookmarks.BuildConfig
import com.knightshrestha.bookmarks.models.Session
import com.knightshrestha.bookmarks.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApolloAuthInterceptor @Inject constructor(private val manager: DataStoreRepository): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()

        val accessToken = runBlocking {
            manager.getAccessToken().first()
        }

        val refreshToken = runBlocking {
            manager.getRefreshToken().first()
        }
        
        Log.d("Auth", accessToken.toString())

        if (accessToken?.let { JWT(it).isExpired(30) } == true) {
            val newToken = getNewToken(refreshToken)

            if (newToken.isSuccessful) {
                runBlocking {
                    manager.saveAccessToken(newToken.body()!!.accessToken)
                }
                val newRequest = request
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${newToken.body()!!.accessToken}")
                    .build()

                return chain.proceed(newRequest)
            }
        }



        request.addHeader("Authorization", "Bearer $accessToken")
        return chain.proceed(request.build())
    }

    private fun getNewToken(refreshToken: String?): retrofit2.Response<Session> {
        val okHttpClient = if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        } else {
            OkHttpClient.Builder().build()
        }


        val retrofit = Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val service = retrofit.create(AuthApiService::class.java)

        return runBlocking { service.refreshToken(
            AuthApiService.RefreshTokenObject(
                refreshToken = "$refreshToken"
            )
        ) }
    }
}