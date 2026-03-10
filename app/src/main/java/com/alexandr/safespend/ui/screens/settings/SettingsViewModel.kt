package com.alexandr.safespend.ui.screens.settings

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr.safespend.BuildConfig
import com.alexandr.safespend.R
import com.alexandr.safespend.data.datastore.SettingsDataStore
import com.alexandr.safespend.data.repository.DayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val version: String = "1.0",
    val isLoading: Boolean = false,
    @param:StringRes val errorResId: Int? = null,
    val resetCompleted: Boolean = false
)

class SettingsViewModel(
    context: Context,
    private val dayRepository: DayRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {
    private val appContext = context.applicationContext
    private val _uiState = MutableStateFlow(
        SettingsUiState(version = resolveVersionName())
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val applicationId: String = BuildConfig.APPLICATION_ID

    fun rateApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = "https://play.google.com/store/apps/details?id=$applicationId".toUri()
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            appContext.startActivity(intent)
        } catch (_: Exception) {
            _uiState.update { it.copy(errorResId = R.string.error_cannot_open_store) }
        }
    }

    fun shareApp() {
        try {
            val text = appContext.getString(R.string.settings_share_message, applicationId)
            val chooserTitle = appContext.getString(R.string.settings_share)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            appContext.startActivity(Intent.createChooser(intent, chooserTitle).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (_: Exception) {
            _uiState.update { it.copy(errorResId = R.string.error_cannot_share_app) }
        }
    }

    fun openPrivacyPolicy() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = appContext.getString(R.string.settings_privacy_url).toUri()
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            appContext.startActivity(intent)
        } catch (_: Exception) {
            _uiState.update { it.copy(errorResId = R.string.error_cannot_open_privacy) }
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorResId = null, resetCompleted = false) }
            try {
                dayRepository.deleteAllDays()
                settingsDataStore.setOnboardingCompleted(false)
                _uiState.update { it.copy(isLoading = false, resetCompleted = true) }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false, errorResId = R.string.error_reset_failed) }
            }
        }
    }

    fun consumeReset() {
        _uiState.update { it.copy(resetCompleted = false) }
    }

    private fun resolveVersionName(): String {
        return runCatching {
            val info = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
            info.versionName.orEmpty().ifBlank { "1.0" }
        }.getOrDefault("1.0")
    }
}
