package com.knightshrestha.bookmarks.core.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(name = "preference")

class DataStoreRepository @Inject constructor(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
        private val USER_ID = stringPreferencesKey("user_ID")
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }
    }fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN]
        }
    }fun getUserID(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_ID]
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = accessToken
        }
    }
    suspend fun saveRefreshToken(refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN] = refreshToken
        }
    }
    suspend fun saveUserID(userID: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userID
        }
    }

    suspend fun deleteAccessToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }

    suspend fun deleteRefreshToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN)
        }
    }

    suspend fun deleteUserID() {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_ID)
        }
    }
}