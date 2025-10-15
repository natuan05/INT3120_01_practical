package com.example.unit6_pathway3_flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unit6_pathway3_flightsearch.data.Airport
import com.example.unit6_pathway3_flightsearch.data.Favorite
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
@Composable
fun FlightSearchApp() {
    val viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    // Dữ liệu yêu thích giờ là List<FullFavoriteFlight>
    val favoriteFlights by viewModel.favoriteFlights.collectAsState()

    Scaffold { innerPadding ->
        FlightSearchScreen(
            uiState = uiState,
            favoriteFlights = favoriteFlights, // Truyền xuống danh sách mới
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onAirportSelected = { viewModel.selectAirport(it) },
            onFavoriteClick = { dep, dest -> viewModel.toggleFavorite(dep, dest) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun FlightSearchScreen(
    uiState: FlightSearchUiState,
    favoriteFlights: List<FullFavoriteFlight>, // <-- SỬA LẠI KIỂU DỮ LIỆU
    onQueryChange: (String) -> Unit,
    onAirportSelected: (Airport) -> Unit,
    onFavoriteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = onQueryChange,
            modifier = Modifier.padding(16.dp)
        )

        val selectedAirport = uiState.selectedAirport

        // Logic hiển thị: Gợi ý, Yêu thích, hoặc Kết quả
        if (uiState.searchQuery.isNotEmpty() && uiState.selectedAirport == null) {
            AirportSuggestions(
                suggestions = uiState.airportSuggestions,
                onAirportSelected = onAirportSelected
            )
        } else if (uiState.searchQuery.isEmpty()) {
            FavoriteFlights(
                favoriteFlights = favoriteFlights,
                onFavoriteClick = onFavoriteClick
            )
        } else  if (selectedAirport != null) { // <-- SỬA LẠI Ở ĐÂY
            FlightList(
                departureAirport = selectedAirport,
                destinationAirports = uiState.flightList,
                favoriteFlights = favoriteFlights,
                onFavoriteClick = onFavoriteClick
            )
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
fun FavoriteFlights(
    favoriteFlights: List<FullFavoriteFlight>,
    onFavoriteClick: (String, String) -> Unit, // Thêm để xử lý việc xóa
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            "Favorite Routes",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyColumn {
            items(favoriteFlights) { flight ->
                Card(
                    modifier = Modifier.padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Khởi hành")
                            Text(
                                "${flight.departure.iataCode} - ${flight.departure.name}",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Điểm đến")
                            Text(
                                "${flight.destination.iataCode} - ${flight.destination.name}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // Nút yêu thích luôn sáng
                        IconButton(onClick = {
                            onFavoriteClick(flight.departure.iataCode, flight.destination.iataCode)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Remove from Favorites",
                                tint = Color.Yellow // Luôn là màu vàng
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun FlightList(
    departureAirport: Airport,
    destinationAirports: List<Airport>,
    favoriteFlights: List<FullFavoriteFlight>,
    onFavoriteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(destinationAirports) { destination ->
            val isFavorite = favoriteFlights.any {
                it.departure.iataCode == departureAirport.iataCode && it.destination.iataCode == destination.iataCode
            }
            Card(
                modifier = Modifier.padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Khởi hành")
                        Text("${departureAirport.iataCode} - ${departureAirport.name}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Điểm đến")
                        Text("${destination.iataCode} - ${destination.name}", fontWeight = FontWeight.Bold)
                    }
                    // TODO: Thêm nút Yêu thích ở đây
                    IconButton(onClick = {
                        onFavoriteClick(departureAirport.iataCode, destination.iataCode)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Yellow else Color.Gray
                        )
                    }
                }
            }
        }
    }
}