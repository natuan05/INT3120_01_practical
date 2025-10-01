package com.example.dessertclicker.ui // Thay bằng package của bạn nếu khác

import androidx.annotation.DrawableRes
import com.example.dessertclicker.data.Datasource.dessertList

/**
 * Data class to represent the state of the Dessert Clicker UI.
 */
data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = 0,
    val currentDessertPrice: Int = dessertList[0].price,
    @DrawableRes val currentDessertImageId: Int = dessertList[0].imageId
)