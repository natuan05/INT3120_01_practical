package com.example.unit6_pathway3_flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unit6_pathway3_flightsearch.data.Airport
import com.example.unit6_pathway3_flightsearch.data.Favorite

@Composable
fun FlightSearchApp() {
    val viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val favoriteFlights by viewModel.favoriteFlights.collectAsState()

    Scaffold { innerPadding ->
        FlightSearchScreen(
            uiState = uiState,
            favoriteFlights = favoriteFlights,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onAirportSelected = { viewModel.selectAirport(it) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun FlightSearchScreen(
    uiState: FlightSearchUiState,
    favoriteFlights: List<Favorite>,
    onQueryChange: (String) -> Unit,
    onAirportSelected: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = onQueryChange,
            modifier = Modifier.padding(16.dp)
        )

        // Logic hiển thị: Gợi ý, Yêu thích, hoặc Kết quả
        if (uiState.searchQuery.isNotEmpty() && uiState.selectedAirport == null) {
            AirportSuggestions(
                suggestions = uiState.airportSuggestions,
                onAirportSelected = onAirportSelected
            )
        } else if (uiState.searchQuery.isEmpty()) {
            FavoriteFlights(favoriteFlights = favoriteFlights)
        } else {
            // Hiển thị danh sách chuyến bay (sẽ làm sau)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Enter airport name") },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun AirportSuggestions(
    suggestions: List<Airport>,
    onAirportSelected: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(suggestions) { airport ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAirportSelected(airport) }
                    .padding(vertical = 8.dp)
            ) {
                Text("${airport.iataCode} - ${airport.name}")
            }
        }
    }
}

@Composable
fun FavoriteFlights(favoriteFlights: List<Favorite>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Favorite Routes")
        LazyColumn {
            items(favoriteFlights) { flight ->
                Card(
                    modifier = Modifier.padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        "${flight.departureCode} -> ${flight.destinationCode}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}