package com.alexandr.safespend.ui.screens.addday

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.alexandr.safespend.R
import com.alexandr.safespend.data.model.DayStatus
import com.alexandr.safespend.ui.components.PrimaryButton
import com.alexandr.safespend.ui.components.SecondaryButton
import com.alexandr.safespend.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddDayScreen(
    onNavigateBack: () -> Unit,
    editDayId: Int? = null,
    viewModel: AddDayViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val theme = LocalAppTheme

    LaunchedEffect(editDayId) {
        viewModel.loadForEdit(editDayId)
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            viewModel.clearSuccess()
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(theme.dimen.md)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.sm)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = theme.colors.textPrimary
                )
            }
            Text(
                text = stringResource(
                    id = if (editDayId == null) R.string.add_day_title else R.string.edit_day_title
                ),
                style = theme.typography.headlineLarge,
                color = theme.colors.textPrimary
            )
        }

        OutlinedTextField(
            value = state.dateInput,
            onValueChange = viewModel::setDateInput,
            label = { Text(stringResource(id = R.string.add_day_date)) },
            modifier = Modifier.fillMaxWidth(),
            supportingText = { Text(stringResource(id = R.string.add_day_date_hint)) },
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.md)
        ) {
            StatusButton(
                text = stringResource(id = R.string.add_day_safe),
                isSelected = state.selectedStatus == DayStatus.SAFE,
                color = theme.colors.success,
                onClick = { viewModel.selectStatus(DayStatus.SAFE) },
                modifier = Modifier.weight(1f)
            )
            StatusButton(
                text = stringResource(id = R.string.add_day_overspend),
                isSelected = state.selectedStatus == DayStatus.OVERSPEND,
                color = theme.colors.error,
                onClick = { viewModel.selectStatus(DayStatus.OVERSPEND) },
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = state.note,
            onValueChange = viewModel::setNote,
            label = { Text(stringResource(id = R.string.add_day_note)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        OutlinedTextField(
            value = state.resilienceScoreInput,
            onValueChange = viewModel::setResilienceScore,
            label = { Text(stringResource(id = R.string.resilience_score)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { Text(stringResource(id = R.string.resilience_score_hint)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        state.errorResId?.let { err ->
            Text(
                text = stringResource(id = err),
                color = theme.colors.error,
                style = theme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.md)
        ) {
            SecondaryButton(
                text = stringResource(id = R.string.add_day_cancel),
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                text = stringResource(id = R.string.add_day_save),
                onClick = viewModel::saveDay,
                modifier = Modifier.weight(1f),
                isLoading = state.isLoading,
                isEnabled = !state.isLoading
            )
        }
    }
}

@Composable
fun StatusButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppTheme
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) color else theme.colors.surface,
                shape = RoundedCornerShape(theme.dimen.md)
            )
            .clickable(onClick = onClick)
            .padding(theme.dimen.md),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else theme.colors.textPrimary,
            style = theme.typography.labelLarge
        )
    }
}
