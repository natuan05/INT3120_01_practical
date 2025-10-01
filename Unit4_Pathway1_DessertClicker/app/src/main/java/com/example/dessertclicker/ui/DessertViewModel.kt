package com.example.dessertclicker.ui // Thay bằng package của bạn nếu khác

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource.dessertList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the Dessert Clicker app.
 */
class DessertViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DessertUiState())

    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    /**
     * Hàm này được gọi mỗi khi người dùng nhấp vào món tráng miệng.
     * Nó sẽ cập nhật doanh thu, số lượng đã bán và món tráng miệng tiếp theo.
     */
    fun onDessertClicked() {
        _uiState.update { currentState ->
            // Cập nhật doanh thu và số lượng đã bán
            val updatedDessertsSold = currentState.dessertsSold + 1
            val newRevenue = currentState.revenue + currentState.currentDessertPrice

            // Tìm món tráng miệng tiếp theo để hiển thị
            val nextDessertIndex = determineNextDessertIndex(updatedDessertsSold)
            val nextDessert = dessertList[nextDessertIndex]

            // Tạo trạng thái mới và trả về
            currentState.copy(
                revenue = newRevenue,
                dessertsSold = updatedDessertsSold,
                currentDessertIndex = nextDessertIndex,
                currentDessertPrice = nextDessert.price,
                currentDessertImageId = nextDessert.imageId
            )
        }
    }

    /**
     * Hàm trợ giúp để xác định món tráng miệng nào sẽ hiển thị
     * dựa trên số lượng đã bán.
     */
    private fun determineNextDessertIndex(dessertsSold: Int): Int {
        var nextDessertIndex = 0
        for (dessert in dessertList) {
            if (dessertsSold >= dessert.startProductionAmount) {
                nextDessertIndex++
            } else {
                // Dừng lại ngay khi tìm thấy món tráng miệng đầu tiên
                // mà chưa đạt đủ số lượng
                break
            }
        }
        return nextDessertIndex
    }
}