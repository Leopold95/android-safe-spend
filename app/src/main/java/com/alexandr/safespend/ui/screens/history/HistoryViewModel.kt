package com.alexandr.safespend.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr.safespend.data.model.DayEntry
import com.alexandr.safespend.data.repository.DayRepository
import com.alexandr.safespend.utils.DateUtils.getMonthStartDate
import com.alexandr.safespend.utils.DateUtils.getWeekStartDate
import com.alexandr.safespend.utils.DateUtils.isIsoOnOrAfter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class DateFilter {
    ALL, WEEK, MONTH
}

data class HistoryUiState(
    val allDays: List<DayEntry> = emptyList(),
    val filteredDays: List<DayEntry> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: DateFilter = DateFilter.ALL,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HistoryViewModel(private val dayRepository: DayRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadDays()
    }

    private fun loadDays() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                dayRepository.getAllDaysFlow().collect { days ->
                    _uiState.update { state ->
                        state.copy(
                            allDays = days,
                            isLoading = false
                        )
                    }
                    applyFilters()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun setFilter(filter: DateFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        var filtered = state.allDays

        when (state.selectedFilter) {
            DateFilter.WEEK -> {
                val startDateIso = getWeekStartDate().toString()
                filtered = filtered.filter { isIsoOnOrAfter(it.date, startDateIso) }
            }
            DateFilter.MONTH -> {
                val startDateIso = getMonthStartDate().toString()
                filtered = filtered.filter { isIsoOnOrAfter(it.date, startDateIso) }
            }
            DateFilter.ALL -> Unit
        }

        val query = state.searchQuery.trim()
        if (query.isNotEmpty()) {
            filtered = filtered.filter { day ->
                day.note.contains(query, ignoreCase = true) ||
                    day.date.contains(query, ignoreCase = true) ||
                    day.status.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(filteredDays = filtered) }
    }
}
