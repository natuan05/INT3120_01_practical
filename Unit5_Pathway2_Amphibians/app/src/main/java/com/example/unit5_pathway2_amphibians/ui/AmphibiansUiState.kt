package com.example.unit5_pathway2_amphibians.ui

import com.example.unit5_pathway2_amphibians.model.Amphibian

/**
 * Một sealed interface đại diện cho các trạng thái của màn hình Amphibians.
 */
sealed interface AmphibiansUiState {
    data class Success(val amphibians: List<Amphibian>) : AmphibiansUiState
    object Error : AmphibiansUiState
    object Loading : AmphibiansUiState
}