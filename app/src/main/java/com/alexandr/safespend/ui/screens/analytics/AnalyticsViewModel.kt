package com.alexandr.safespend.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr.safespend.data.model.AnalyticsData
import com.alexandr.safespend.data.repository.DayRepository
import com.alexandr.safespend.utils.DateUtils.getMonthStartDate
import com.alexandr.safespend.utils.DateUtils.getWeekStartDate
import com.alexandr.safespend.utils.DateUtils.getYearStartDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    private var analyticsJob: Job? = null

    init {
        loadAnalytics(AnalyticsPeriod.MONTH)
    }

    private fun loadAnalytics(period: AnalyticsPeriod) {
        analyticsJob?.cancel()
        analyticsJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val startDate = when (period) {
                AnalyticsPeriod.WEEK -> getWeekStartDate()
                AnalyticsPeriod.MONTH -> getMonthStartDate()
                AnalyticsPeriod.YEAR -> getYearStartDate()
            }
            val endDate = LocalDate.now()
            try {
                dayRepository.getAnalyticsFlow(startDate = startDate, endDate = endDate).collect { analytics ->
                    _uiState.update {
                        it.copy(
                            analytics = analytics,
                            selectedPeriod = period,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun setPeriod(period: AnalyticsPeriod) {
        if (_uiState.value.selectedPeriod == period) return
        _uiState.update { it.copy(selectedPeriod = period) }
        loadAnalytics(period)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
