package com.example.unit5_pathway2_bookshelf.ui // Thay bằng package của bạn

/**
 * Một sealed interface đại diện cho các trạng thái của màn hình Bookshelf.
 */
sealed interface BookshelfUiState {
    /**
     * Trạng thái thành công, chứa một danh sách các URL hình ảnh.
     */
    data class Success(val photos: List<String>) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
}