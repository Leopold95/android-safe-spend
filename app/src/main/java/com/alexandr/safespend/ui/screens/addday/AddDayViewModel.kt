package com.alexandr.safespend.ui.screens.addday

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr.safespend.R
import com.alexandr.safespend.data.model.DayStatus
import com.alexandr.safespend.data.repository.DayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeParseException

data class AddDayUiState(
    val editDayId: Int? = null,
    val dateInput: String = LocalDate.now().toString(),
    val selectedStatus: DayStatus? = null,
    val note: String = "",
    val resilienceScoreInput: String = "",
    val resilienceScore: Int? = null,
    val isLoading: Boolean = false,
    @param:StringRes val errorResId: Int? = null,
    val success: Boolean = false
)

class AddDayViewModel(private val dayRepository: DayRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AddDayUiState())
    val uiState: StateFlow<AddDayUiState> = _uiState.asStateFlow()

    fun loadForEdit(dayId: Int?) {
        if (dayId == null || _uiState.value.editDayId == dayId) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorResId = null) }
            val day = dayRepository.getDayById(dayId)
            if (day == null) {
                _uiState.update { it.copy(isLoading = false, errorResId = R.string.error_no_data) }
                return@launch
            }
            _uiState.update {
                it.copy(
                    editDayId = day.id,
                    dateInput = day.date,
                    selectedStatus = day.status,
                    note = day.note,
                    resilienceScoreInput = day.resilienceScore?.toString().orEmpty(),
                    resilienceScore = day.resilienceScore,
                    isLoading = false,
                    errorResId = null
                )
            }
        }
    }

    fun selectStatus(status: DayStatus) {
        _uiState.update { it.copy(selectedStatus = status) }
    }

    fun setDateInput(date: String) {
        _uiState.update { it.copy(dateInput = date.take(10)) }
    }

    fun setNote(note: String) {
        _uiState.update { it.copy(note = note.take(200)) }
    }

    fun setResilienceScore(score: String) {
        val cleaned = score.filter { it.isDigit() }.take(3)
        val intScore = cleaned.toIntOrNull()?.coerceIn(0, 100)
        _uiState.update { it.copy(resilienceScoreInput = cleaned, resilienceScore = intScore) }
    }

    fun saveDay() {
        val state = _uiState.value
        val status = state.selectedStatus
        if (status == null) {
            _uiState.update { it.copy(errorResId = R.string.error_select_status) }
            return
        }

        val parsedDate = try {
            LocalDate.parse(state.dateInput)
        } catch (_: DateTimeParseException) {
            null
        }

        if (parsedDate == null) {
            _uiState.update { it.copy(errorResId = R.string.error_invalid_date) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorResId = null) }
            try {
                val currentId = state.editDayId
                if (currentId != null) {
                    dayRepository.updateDay(
                        id = currentId,
                        date = parsedDate,
                        status = status,
                        note = state.note,
                        resilienceScore = state.resilienceScore
                    )
                } else {
                    val existingDay = dayRepository.getDayByDate(parsedDate)
                    if (existingDay != null) {
                        dayRepository.updateDay(
                            id = existingDay.id,
                            date = parsedDate,
                            status = status,
                            note = state.note,
                            resilienceScore = state.resilienceScore
                        )
                    } else {
                        dayRepository.addDay(
                            date = parsedDate,
                            status = status,
                            note = state.note,
                            resilienceScore = state.resilienceScore
                        )
                    }
                }
                _uiState.update { it.copy(success = true, isLoading = false) }
            } catch (_: Exception) {
                _uiState.update { it.copy(errorResId = R.string.add_day_error, isLoading = false) }
            }
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(success = false) }
    }
}
