package com.example.unit6_pathway3_flightsearch.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    /**
     * Lấy từ khóa tìm kiếm đã lưu dưới dạng một Flow.
     */
    val searchInput: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SEARCH_INPUT_KEY] ?: ""
        }

    /**
     * Lưu từ khóa tìm kiếm mới.
     */
    suspend fun saveSearchInput(searchInput: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_INPUT_KEY] = searchInput
        }
    }

    private companion object {
        val SEARCH_INPUT_KEY = stringPreferencesKey("search_input")
        const val TAG = "UserPreferencesRepo"
    }
}