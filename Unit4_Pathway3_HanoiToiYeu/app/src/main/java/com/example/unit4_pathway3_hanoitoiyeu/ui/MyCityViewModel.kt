package com.example.unit4_pathway3_hanoitoiyeu.ui

import androidx.lifecycle.ViewModel
import com.example.unit4_pathway3_hanoitoiyeu.data.Category
import com.example.unit4_pathway3_hanoitoiyeu.data.LocalDataProvider
import com.example.unit4_pathway3_hanoitoiyeu.data.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyCityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MyCityUiState())
    val uiState: StateFlow<MyCityUiState> = _uiState.asStateFlow()

    // Lấy danh sách tất cả các danh mục từ DataProvider
    val categories = LocalDataProvider.categories

    /**
     * Cập nhật trạng thái khi người dùng chọn một danh mục.
     */
    fun selectCategory(category: Category) {
        _uiState.update {
            it.copy(selectedCategory = category, selectedPlace = null)
        }
    }

    /**
     * Cập nhật trạng thái khi người dùng chọn một địa điểm.
     */
    fun selectPlace(place: Place) {
        _uiState.update {
            it.copy(selectedPlace = place)
        }
    }

    /**
     * Quay lại màn hình danh sách các danh mục.
     */
    fun navigateToCategories() {
        _uiState.update {
            it.copy(selectedCategory = null, selectedPlace = null)
        }
    }

    /**
     * Quay lại màn hình danh sách các địa điểm.
     */
    fun navigateToPlaces() {
        _uiState.update {
            it.copy(selectedPlace = null)
        }
    }
}