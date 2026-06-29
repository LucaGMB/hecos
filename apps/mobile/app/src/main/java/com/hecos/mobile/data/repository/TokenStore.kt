package com.hecos.mobile.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "hecos_prefs")

class TokenStore(private val context: Context) {

    companion object {
        private val JWT_KEY = stringPreferencesKey("jwt_token")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }

    suspend fun saveSession(token: String, email: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[JWT_KEY] = token
            prefs[USER_EMAIL_KEY] = email
            prefs[USER_NAME_KEY] = name
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { it[JWT_KEY] }.first()
    }

    suspend fun getUserEmail(): String? {
        return context.dataStore.data.map { it[USER_EMAIL_KEY] }.first()
    }

    suspend fun getUserName(): String? {
        return context.dataStore.data.map { it[USER_NAME_KEY] }.first()
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun hasSession(): Boolean {
        return getToken() != null
    }
}
