package com.knightshrestha.bookmarks.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.knightshrestha.bookmarks.BuildConfig
import com.knightshrestha.bookmarks.DATA_URL
import com.knightshrestha.bookmarks.repository.DataStoreRepository
import com.knightshrestha.bookmarks.services.ApolloAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

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
        return ApolloClient.Builder()
            .serverUrl(DATA_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

}