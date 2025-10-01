package com.example.unit4_pathway3_hanoitoiyeu.ui

import com.example.unit4_pathway3_hanoitoiyeu.data.Category
import com.example.unit4_pathway3_hanoitoiyeu.data.Place

/**
 * Data class để lưu trữ trạng thái hiện tại của giao diện người dùng.
 */
data class MyCityUiState(
    // Danh mục đang được chọn. Nếu null, hiển thị danh sách các danh mục.
    val selectedCategory: Category? = null,
    // Địa điểm đang được chọn. Nếu null, hiển thị danh sách các địa điểm.
    val selectedPlace: Place? = null
)