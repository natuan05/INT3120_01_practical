package com.example.unit6_pathway3_flightsearch.ui // Thay bằng package của bạn

import com.example.unit6_pathway3_flightsearch.data.Airport
import com.example.unit6_pathway3_flightsearch.data.Favorite

data class FlightSearchUiState(
    val searchQuery: String = "",
    val selectedAirport: Airport? = null,
    val airportSuggestions: List<Airport> = emptyList(),
    val flightList: List<Airport> = emptyList(),
    val favoriteFlights: List<Favorite> = emptyList()
)


data class FullFavoriteFlight(
    val departure: Airport,
    val destination: Airport
)