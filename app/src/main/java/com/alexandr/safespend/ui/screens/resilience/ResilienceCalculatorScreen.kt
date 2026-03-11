package com.alexandr.safespend.ui.screens.resilience

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.alexandr.safespend.R
import com.alexandr.safespend.ui.components.PrimaryButton
import com.alexandr.safespend.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResilienceCalculatorScreen(
    onNavigateBack: () -> Unit,
    viewModel: ResilienceCalculatorViewModel = koinViewModel()
) {
    val theme = LocalAppTheme
    val state = viewModel.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(theme.dimen.md)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.sm)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = theme.colors.textPrimary
                )
            }

            Text(
                text = stringResource(R.string.resilience_title),
                style = theme.typography.headlineLarge,
                color = theme.colors.textPrimary
            )
        }

        OutlinedTextField(
            value = state.dateInput,
            onValueChange = viewModel::setDateInput,
            label = { Text(stringResource(R.string.resilience_date)) },
            supportingText = { Text(stringResource(R.string.add_day_date_hint)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.scoreInput,
            onValueChange = viewModel::setScore,
            label = { Text(stringResource(R.string.resilience_score)) },
            supportingText = { Text(stringResource(R.string.resilience_score_hint)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.note,
            onValueChange = viewModel::setNote,
            label = { Text(stringResource(R.string.resilience_note)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        PrimaryButton(
            text = stringResource(R.string.resilience_save_result),
            onClick = viewModel::saveResult,
            isLoading = state.isLoading,
            isEnabled = !state.isLoading
        )

        state.savedScore?.let { savedScore ->
            Text(
                text = stringResource(R.string.resilience_result_format, savedScore),
                style = theme.typography.titleLarge,
                color = theme.colors.primaryAccent
            )
        }

        if (state.wasSaved) {
            Text(
                text = stringResource(R.string.resilience_saved_message),
                style = theme.typography.bodyMedium,
                color = theme.colors.success
            )
        }

        state.errorResId?.let { errorResId ->
            Text(
                text = stringResource(errorResId),
                style = theme.typography.bodySmall,
                color = theme.colors.error
            )
        }
    }
}
