package com.alexandr.safespend.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexandr.safespend.R
import com.alexandr.safespend.ui.components.SettingsItem
import com.alexandr.safespend.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onNavigateHomeAfterReset: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val theme = LocalAppTheme
    var showResetDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.resetCompleted) {
        if (state.resetCompleted) {
            viewModel.consumeReset()
            onNavigateHomeAfterReset()
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.settings_reset)) },
            text = { Text(stringResource(R.string.settings_confirm_reset)) },
            confirmButton = {
                TextButton(onClick = {
                    showResetDialog = false
                    viewModel.resetAllData()
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(theme.dimen.md)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
    ) {
        Text(
            text = stringResource(id = R.string.settings_title),
            style = theme.typography.headlineLarge,
            color = theme.colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider()

        SettingsItem(
            title = stringResource(id = R.string.settings_rate),
            icon = Icons.Default.ChevronRight,
            onClick = viewModel::rateApp
        )

        SettingsItem(
            title = stringResource(id = R.string.settings_share),
            icon = Icons.Default.ChevronRight,
            onClick = viewModel::shareApp
        )

        SettingsItem(
            title = stringResource(id = R.string.settings_privacy),
            icon = Icons.Default.ChevronRight,
            onClick = viewModel::openPrivacyPolicy
        )

        SettingsItem(
            title = stringResource(id = R.string.settings_reset),
            icon = Icons.Default.ChevronRight,
            onClick = { showResetDialog = true }
        )

        HorizontalDivider()

        Text(
            text = stringResource(id = R.string.settings_version, state.version),
            style = theme.typography.bodySmall,
            color = theme.colors.textSecondary,
            modifier = Modifier.padding(start = theme.dimen.md)
        )

        state.errorResId?.let { err ->
            Text(
                text = stringResource(err),
                color = theme.colors.error,
                style = theme.typography.bodySmall
            )
        }
    }
}
