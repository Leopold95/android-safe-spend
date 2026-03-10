package com.alexandr.safespend.ui.screens.resilience

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr.safespend.R
import com.alexandr.safespend.data.repository.DayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeParseException

data class ResilienceCalculatorUiState(
    val dateInput: String = LocalDate.now().toString(),
    val scoreInput: String = "",
    val note: String = "",
    val savedScore: Int? = null,
    val isLoading: Boolean = false,
    @param:StringRes val errorResId: Int? = null,
    val wasSaved: Boolean = false
)

class ResilienceCalculatorViewModel(
    private val dayRepository: DayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResilienceCalculatorUiState())
    val uiState: StateFlow<ResilienceCalculatorUiState> = _uiState.asStateFlow()

    fun setDateInput(value: String) {
        _uiState.update { it.copy(dateInput = value.take(10), errorResId = null, wasSaved = false) }
    }

    fun setScore(value: String) {
        val cleaned = value.filter { it.isDigit() }.take(3)
        _uiState.update { it.copy(scoreInput = cleaned, errorResId = null, wasSaved = false) }
    }

    fun setNote(value: String) {
        _uiState.update { it.copy(note = value.take(200), errorResId = null, wasSaved = false) }
    }

    fun saveResult() {
        val state = _uiState.value
        val parsedDate = try {
            LocalDate.parse(state.dateInput)
        } catch (_: DateTimeParseException) {
            null
        }

        if (parsedDate == null) {
            _uiState.update { it.copy(errorResId = R.string.error_invalid_date) }
            return
        }

        val score = state.scoreInput.toIntOrNull()?.coerceIn(0, 100)
        if (score == null) {
            _uiState.update { it.copy(errorResId = R.string.resilience_error_invalid_score) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorResId = null) }
            try {
                dayRepository.saveResilienceCheck(
                    date = parsedDate,
                    score = score,
                    note = state.note
                )
                _uiState.update {
                    it.copy(
                        savedScore = score,
                        isLoading = false,
                        wasSaved = true,
                        errorResId = null
                    )
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false, errorResId = R.string.add_day_error) }
            }
        }
    }
}

