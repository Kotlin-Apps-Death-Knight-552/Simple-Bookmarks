package com.knightshrestha.bookmarks.mainscreen.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.knightshrestha.bookmarks.BuildConfig
import com.knightshrestha.bookmarks.authentication.services.ApolloAuthInterceptor
import com.knightshrestha.bookmarks.core.helpers.DATA_URL
import com.knightshrestha.bookmarks.core.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton
import kotlin.math.pow

@InstallIn(SingletonComponent::class)
@Module
object ApolloDI {
    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: ApolloAuthInterceptor): OkHttpClient {
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(manager: DataStoreRepository): ApolloAuthInterceptor = ApolloAuthInterceptor(manager)

    @Singleton
    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.Builder().useV3ExceptionHandling(true)
            .serverUrl(DATA_URL)
            .webSocketReopenWhen { throwable, attempt ->
                kotlinx.coroutines.delay(2.0.pow(attempt.toDouble()).toLong())
                true
            }
            .okHttpClient(okHttpClient)
            .build()
    }

}