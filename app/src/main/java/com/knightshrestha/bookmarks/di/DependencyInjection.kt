package com.knightshrestha.bookmarks.di

import android.content.Context
import com.knightshrestha.bookmarks.helpers.AUTH_URL
import com.knightshrestha.bookmarks.BuildConfig
import com.knightshrestha.bookmarks.services.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object DependencyInjection {

    @Provides
    fun provideRetrofit(): Retrofit {
        val httpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            httpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        return Retrofit.Builder()
            .client(httpClientBuilder.build())
            .baseUrl(AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(
            AuthApiService::class.java
        )
    }

    @Provides
    fun provideContext(@ApplicationContext a: Context): Context {
        return a
    }


}