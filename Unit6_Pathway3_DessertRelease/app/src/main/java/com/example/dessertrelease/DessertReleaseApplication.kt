package com.example.dessertrelease

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dessertrelease.data.UserPreferencesRepository

// Tên của tệp DataStore
private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"

// Tạo một thuộc tính mở rộng (extension property) để tạo DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

class DessertReleaseApplication : Application() {
    /**
     * AppContainer instance được sử dụng bởi các lớp khác để lấy phần phụ thuộc.
     */
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}