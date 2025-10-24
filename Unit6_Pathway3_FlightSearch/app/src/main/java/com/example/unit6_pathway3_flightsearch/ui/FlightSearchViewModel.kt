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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map

class FlightSearchViewModel(
    private val flightDao: FlightDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightSearchUiState())
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()

    // StateFlow để giữ danh sách yêu thích
    val favoriteFlights: StateFlow<List<FullFavoriteFlight>> =
        flightDao.getAllFavorites()
            .map { favoriteList ->
                favoriteList.mapNotNull { favorite ->
                    val departure = flightDao.getAirportByCodeSuspend(favorite.departureCode)
                    val destination = flightDao.getAirportByCodeSuspend(favorite.destinationCode)
                    if (departure != null && destination != null) {
                        FullFavoriteFlight(departure, destination)
                    } else {
                        null
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = emptyList()
            )

    init {
        viewModelScope.launch {
            val savedSearch = userPreferencesRepository.searchInput.first()
            _uiState.value = FlightSearchUiState(searchQuery = savedSearch)
            updateAirportSuggestions(savedSearch)
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            // Cập nhật từ khóa tìm kiếm và RESET sân bay đã chọn
            _uiState.update {
                it.copy(
                    searchQuery = query,
                    selectedAirport = null
                )
            }
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
        viewModelScope.launch {
            // Lấy danh sách các điểm đến khả thi từ DAO
            val destinations = flightDao.getPossibleDestinations(airport.iataCode).first()
            _uiState.update {
                it.copy(
                    selectedAirport = airport,
                    searchQuery = airport.iataCode,
                    airportSuggestions = emptyList(),
                    flightList = destinations // Cập nhật danh sách chuyến bay
                )
            }
        }
    }

    fun addFavorite(flight: Favorite) {
        viewModelScope.launch {
            flightDao.addFavorite(flight)
        }
    }

    fun removeFavorite(flight: Favorite) {
        viewModelScope.launch {
            flightDao.removeFavorite(flight)
        }
    }

    fun toggleFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val flight = flightDao.getAllFavorites().first().find {
                it.departureCode == departureCode && it.destinationCode == destinationCode
            }
            if (flight == null) {
                // Thêm vào yêu thích
                flightDao.addFavorite(
                    Favorite(departureCode = departureCode, destinationCode = destinationCode)
                )
            } else {
                // Xóa khỏi yêu thích bằng hàm mới
                flightDao.deleteFavorite(
                    departureCode = departureCode,
                    destinationCode = destinationCode
                )
            }
        }
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