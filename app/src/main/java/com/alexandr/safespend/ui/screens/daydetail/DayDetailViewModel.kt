package com.alexandr.safespend.ui.screens.daydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr.safespend.data.model.DayEntry
import com.alexandr.safespend.data.repository.DayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DayDetailUiState(
    val dayEntry: DayEntry? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDeleted: Boolean = false
)

class DayDetailViewModel(
    private val dayRepository: DayRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DayDetailUiState())
    val uiState: StateFlow<DayDetailUiState> = _uiState.asStateFlow()

    fun loadDay(dayId: Int) {
        if (dayId == 0) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val day = dayRepository.getDayById(dayId)
                _uiState.update { it.copy(dayEntry = day, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteDay(dayEntry: DayEntry) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                dayRepository.deleteDay(dayEntry)
                _uiState.update { it.copy(isDeleted = true, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

