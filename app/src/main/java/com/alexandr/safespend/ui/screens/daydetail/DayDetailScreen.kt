package com.alexandr.safespend.ui.screens.daydetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexandr.safespend.R
import com.alexandr.safespend.data.model.DayStatus
import com.alexandr.safespend.ui.components.PrimaryButton
import com.alexandr.safespend.ui.components.SecondaryButton
import com.alexandr.safespend.ui.theme.LocalAppTheme
import com.alexandr.safespend.utils.DateUtils.formatIsoDateForDisplay
import org.koin.androidx.compose.koinViewModel

@Composable
fun DayDetailScreen(
    dayId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEditDay: (Int) -> Unit,
    viewModel: DayDetailViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val theme = LocalAppTheme

    LaunchedEffect(dayId) {
        viewModel.loadDay(dayId)
    }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onNavigateBack()
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize().padding(theme.dimen.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = theme.colors.primaryAccent)
        }
        return
    }

    if (state.dayEntry == null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(theme.dimen.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.error_no_data),
                color = theme.colors.textSecondary,
                style = theme.typography.bodyMedium
            )
            PrimaryButton(
                text = stringResource(id = R.string.back),
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            )
        }
        return
    }

    val day = state.dayEntry
    val dateStr = formatIsoDateForDisplay(day.date)
    val statusRes = if (day.status == DayStatus.SAFE) R.string.status_safe else R.string.status_overspend

    Column(
        modifier = Modifier.fillMaxSize().padding(theme.dimen.md),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
    ) {
        Row(
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
                text = stringResource(id = R.string.day_detail_title),
                style = theme.typography.headlineLarge,
                color = theme.colors.textPrimary
            )
        }

        Text(
            text = dateStr,
            style = theme.typography.titleMedium,
            color = theme.colors.textSecondary
        )

        Text(
            text = stringResource(statusRes),
            style = theme.typography.titleLarge,
            color = if (day.status == DayStatus.SAFE) theme.colors.success else theme.colors.error
        )

        if (day.note.isNotBlank()) {
            Column(verticalArrangement = Arrangement.spacedBy(theme.dimen.sm)) {
                Text(
                    text = stringResource(id = R.string.day_detail_note),
                    style = theme.typography.labelLarge,
                    color = theme.colors.textSecondary
                )
                Text(
                    text = day.note,
                    style = theme.typography.bodyMedium,
                    color = theme.colors.textPrimary
                )
            }
        }

        day.resilienceScore?.let { score ->
            Column(verticalArrangement = Arrangement.spacedBy(theme.dimen.sm)) {
                Text(
                    text = stringResource(id = R.string.day_detail_resilience),
                    style = theme.typography.labelLarge,
                    color = theme.colors.textSecondary
                )
                Text(
                    text = stringResource(R.string.resilience_result_format, score),
                    style = theme.typography.bodyMedium,
                    color = theme.colors.textPrimary
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = theme.dimen.md),
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.md)
        ) {
            SecondaryButton(
                text = stringResource(id = R.string.day_detail_edit),
                onClick = { onNavigateToEditDay(day.id) },
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                text = stringResource(id = R.string.day_detail_delete),
                onClick = { viewModel.deleteDay(day) },
                modifier = Modifier.weight(1f)
            )
        }

        state.error?.let {
            Text(
                text = it,
                color = theme.colors.error,
                style = theme.typography.bodySmall
            )
        }
    }
}
