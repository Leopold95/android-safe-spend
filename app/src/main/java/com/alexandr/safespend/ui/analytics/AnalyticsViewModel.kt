package com.alexandr.safespend.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr.safespend.data.model.AnalyticsData
import com.alexandr.safespend.data.repository.DayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AnalyticsPeriod {
    WEEK, MONTH, YEAR
}

data class AnalyticsUiState(
    val analytics: AnalyticsData = AnalyticsData(),
    val selectedPeriod: AnalyticsPeriod = AnalyticsPeriod.MONTH,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AnalyticsViewModel(private val dayRepository: DayRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                dayRepository.getAnalyticsFlow().collect { analytics ->
                    _uiState.update { it.copy(analytics = analytics, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun setPeriod(period: AnalyticsPeriod) {
        _uiState.update { it.copy(selectedPeriod = period) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

