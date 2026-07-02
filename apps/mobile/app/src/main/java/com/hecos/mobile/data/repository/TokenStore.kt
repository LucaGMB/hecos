package com.hecos.mobile.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
        private val AUTO_SYNC_ENABLED_KEY = booleanPreferencesKey("auto_sync_enabled")
        private val AUTO_SYNC_INTERVAL_HOURS_KEY = intPreferencesKey("auto_sync_interval_hours")

        const val DEFAULT_AUTO_SYNC_INTERVAL_HOURS = 6
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

    suspend fun setAutoSyncEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[AUTO_SYNC_ENABLED_KEY] = enabled }
    }

    suspend fun isAutoSyncEnabled(): Boolean {
        return context.dataStore.data.map { it[AUTO_SYNC_ENABLED_KEY] ?: false }.first()
    }

    suspend fun setAutoSyncIntervalHours(hours: Int) {
        context.dataStore.edit { prefs -> prefs[AUTO_SYNC_INTERVAL_HOURS_KEY] = hours }
    }

    suspend fun getAutoSyncIntervalHours(): Int {
        return context.dataStore.data
            .map { it[AUTO_SYNC_INTERVAL_HOURS_KEY] ?: DEFAULT_AUTO_SYNC_INTERVAL_HOURS }
            .first()
    }
}
