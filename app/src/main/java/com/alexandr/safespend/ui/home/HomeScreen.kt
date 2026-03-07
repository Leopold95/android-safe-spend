package com.alexandr.safespend.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexandr.safespend.R
import com.alexandr.safespend.data.model.DayStatus
import com.alexandr.safespend.ui.components.ActionButton
import com.alexandr.safespend.ui.components.SettingsItem
import com.alexandr.safespend.ui.components.StatCard
import com.alexandr.safespend.ui.components.StreakCard
import com.alexandr.safespend.ui.theme.LocalAppTheme
import com.alexandr.safespend.utils.DateUtils.formatIsoDateForDisplay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToAddDay: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToCalculator: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val theme = LocalAppTheme

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(theme.dimen.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = theme.colors.primaryAccent)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(theme.dimen.md),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.home_title),
                style = theme.typography.headlineLarge,
                color = theme.colors.textPrimary
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.home_motivation),
                style = theme.typography.bodyMedium,
                color = theme.colors.textSecondary
            )
        }

        item {
            StreakCard(
                streak = state.analytics.currentStreak,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(theme.dimen.md)
            ) {
                StatCard(
                    title = stringResource(id = R.string.analytics_safe_days),
                    value = state.analytics.totalSafeDays.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = stringResource(id = R.string.analytics_overspend_days),
                    value = state.analytics.totalOverspendDays.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            ActionButton(
                text = stringResource(id = R.string.home_add_day),
                icon = Icons.Default.Add,
                onClick = onNavigateToAddDay
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(theme.dimen.md)
            ) {
                ActionButton(
                    text = stringResource(id = R.string.nav_history),
                    icon = Icons.AutoMirrored.Filled.List,
                    onClick = onNavigateToHistory,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = stringResource(id = R.string.nav_analytics),
                    icon = Icons.Default.BarChart,
                    onClick = onNavigateToAnalytics,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(theme.dimen.md)
            ) {
                ActionButton(
                    text = stringResource(id = R.string.resilience_title),
                    icon = Icons.Default.Calculate,
                    onClick = onNavigateToCalculator,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = stringResource(id = R.string.nav_settings),
                    icon = Icons.Default.Settings,
                    onClick = onNavigateToSettings,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.home_recent),
                style = theme.typography.titleLarge,
                color = theme.colors.textPrimary
            )
        }

        val recentDays = state.analytics.allDays.take(5)
        if (recentDays.isEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.home_no_data),
                    style = theme.typography.bodyMedium,
                    color = theme.colors.textSecondary
                )
            }
        } else {
            items(recentDays, key = { it.id }) { day ->
                val date = formatIsoDateForDisplay(day.date)
                val status = stringResource(
                    id = if (day.status == DayStatus.SAFE) R.string.status_safe else R.string.status_overspend
                )
                SettingsItem(
                    title = date,
                    subtitle = if (day.note.isBlank()) status else "$status • ${day.note}",
                    onClick = onNavigateToHistory
                )
            }
        }

        state.error?.let { msg ->
            item {
                Text(
                    text = msg,
                    color = theme.colors.error,
                    style = theme.typography.bodySmall
                )
            }
        }
    }
}
