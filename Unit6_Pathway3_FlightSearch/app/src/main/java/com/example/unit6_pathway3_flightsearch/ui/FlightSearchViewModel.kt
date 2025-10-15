package com.example.unit6_pathway3_flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unit6_pathway3_flightsearch.FlightSearchApplication
import com.example.unit6_pathway3_flightsearch.data.Airport
import com.example.unit6_pathway3_flightsearch.data.Favorite
import com.example.unit6_pathway3_flightsearch.data.FlightDao
import com.example.unit6_pathway3_flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightSearchViewModel(
    private val flightDao: FlightDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightSearchUiState())
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()

    // StateFlow để giữ danh sách yêu thích
    val favoriteFlights: StateFlow<List<Favorite>> =
        flightDao.getAllFavorites().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            // Đọc từ khóa tìm kiếm đã lưu khi khởi tạo
            val savedSearch = userPreferencesRepository.searchInput.first()
            _uiState.value = FlightSearchUiState(searchQuery = savedSearch)
            updateAirportSuggestions(savedSearch)
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        viewModelScope.launch {
            userPreferencesRepository.saveSearchInput(query)
            updateAirportSuggestions(query)
        }
    }

    private suspend fun updateAirportSuggestions(query: String) {
        if (query.isNotEmpty()) {
            val suggestions = flightDao.getAirportsByQuery(query).first()
            _uiState.value = _uiState.value.copy(airportSuggestions = suggestions)
        } else {
            _uiState.value = _uiState.value.copy(airportSuggestions = emptyList())
        }
    }

    fun selectAirport(airport: Airport) {
        _uiState.value = _uiState.value.copy(
            selectedAirport = airport,
            searchQuery = airport.iataCode,
            airportSuggestions = emptyList()
        )
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApplication)
                FlightSearchViewModel(
                    flightDao = application.database.flightDao(),
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}