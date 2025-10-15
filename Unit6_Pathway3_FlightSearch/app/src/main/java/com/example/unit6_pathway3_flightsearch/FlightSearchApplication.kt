package com.example.unit6_pathway3_flightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.unit6_pathway3_flightsearch.data.FlightSearchDatabase
import com.example.unit6_pathway3_flightsearch.data.UserPreferencesRepository

// Tên của tệp DataStore
private const val SEARCH_PREFERENCE_NAME = "search_preference"

// Tạo một thuộc tính mở rộng để tạo DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SEARCH_PREFERENCE_NAME
)

class FlightSearchApplication : Application() {
    // Khởi tạo cơ sở dữ liệu Room một cách lười biếng
    val database: FlightSearchDatabase by lazy { FlightSearchDatabase.getDatabase(this) }

    // Khởi tạo repository cho DataStore
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}