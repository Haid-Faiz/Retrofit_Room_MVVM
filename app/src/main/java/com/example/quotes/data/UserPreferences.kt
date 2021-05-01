package com.example.quotes.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app_preferences")

class UserPreferences(context: Context) {

    private val _dataStore: DataStore<Preferences> = context.dataStore

    val authToken: Flow<String?>
        get() = _dataStore.data.map {
            it[KEY_AUTH]
        }

    suspend fun saveAuthToken(authToken: String) {
        _dataStore.edit {
            it[KEY_AUTH] = authToken
        }
    }

    suspend fun clearAuthToken() {
        _dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val KEY_AUTH: Preferences.Key<String> = stringPreferencesKey("key_auth")
    }
}