package com.example.unit3_pathway3_30days

import androidx.lifecycle.ViewModel
import com.example.unit3_pathway3_30days.model.DayTip
import com.example.unit3_pathway3_30days.model.TipsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ThirtyDaysViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ThirtyDaysUiState())
    val uiState: StateFlow<ThirtyDaysUiState> = _uiState.asStateFlow()

    fun toggleExpansion(dayNum: Int) {
        _uiState.update { currentState ->
            val updatedTips = currentState.tips.map { tip ->
                if (tip.day == dayNum) {
                    tip.copy(isExpanded = !tip.isExpanded)
                } else {
                    tip
                }
            }
            currentState.copy(tips = updatedTips)
        }
    }
}

data class ThirtyDaysUiState(
    val tips: List<DayTip> = TipsRepository.tips
)
