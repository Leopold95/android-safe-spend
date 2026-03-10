package com.alexandr.safespend.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexandr.safespend.R
import com.alexandr.safespend.data.model.DayEntry
import com.alexandr.safespend.data.model.DayStatus
import com.alexandr.safespend.ui.components.SettingsItem
import com.alexandr.safespend.ui.theme.LocalAppTheme
import com.alexandr.safespend.utils.DateUtils.formatIsoDateForDisplay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreen(
    onNavigateToDayDetail: (Int) -> Unit,
    onNavigateToAddDay: () -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val theme = LocalAppTheme

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

    Column(
        modifier = Modifier.fillMaxSize().padding(theme.dimen.md),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.history_title),
                style = theme.typography.headlineLarge,
                color = theme.colors.textPrimary
            )

            IconButton(onClick = onNavigateToAddDay) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.home_add_day),
                    tint = theme.colors.primaryAccent
                )
            }
        }

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = viewModel::setSearchQuery,
            label = { Text(stringResource(id = R.string.history_search_hint)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.sm)
        ) {
            DateFilter.entries.forEach { filter ->
                FilterChip(
                    selected = state.selectedFilter == filter,
                    onClick = { viewModel.setFilter(filter) },
                    label = { Text(filter.toLabel()) }
                )
            }
        }

        if (state.filteredDays.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(theme.dimen.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.history_empty),
                    color = theme.colors.textSecondary,
                    style = theme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(theme.dimen.sm)) {
                items(state.filteredDays, key = { it.id }) { day ->
                    HistoryListItem(
                        day = day,
                        onClick = { onNavigateToDayDetail(day.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryListItem(
    day: DayEntry,
    onClick: () -> Unit
) {
    val dateStr = formatIsoDateForDisplay(day.date)
    val statusRes = if (day.status == DayStatus.SAFE) R.string.status_safe else R.string.status_overspend

    SettingsItem(
        title = "$dateStr (${stringResource(statusRes)})",
        subtitle = day.note.ifBlank { stringResource(R.string.history_no_note) },
        onClick = onClick
    )
}

@Composable
private fun DateFilter.toLabel(): String {
    return when (this) {
        DateFilter.ALL -> stringResource(R.string.history_all)
        DateFilter.WEEK -> stringResource(R.string.history_week)
        DateFilter.MONTH -> stringResource(R.string.history_month)
    }
}
