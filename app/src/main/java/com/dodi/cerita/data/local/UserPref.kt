package com.dodi.cerita.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPref @Inject constructor(private val store: DataStore<Preferences>) {
    private var TOKEN = stringPreferencesKey("token")

    suspend fun setToken(token: String) {
        store.edit { pref ->
            pref[TOKEN] = token
        }
    }

    fun getToken(): Flow<String?> {
        return store.data.map { pref ->
            pref[TOKEN]
        }
    }
}