package com.alexandr.safespend.ui.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexandr.safespend.R
import com.alexandr.safespend.ui.components.StatCard
import com.alexandr.safespend.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = koinViewModel()
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
        modifier = Modifier
            .fillMaxSize()
            .padding(theme.dimen.md)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.analytics_title),
            style = theme.typography.headlineLarge,
            color = theme.colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.sm)
        ) {
            AnalyticsPeriod.entries.forEach { period ->
                FilterChip(
                    selected = state.selectedPeriod == period,
                    onClick = { viewModel.setPeriod(period) },
                    label = { Text(period.toLabel()) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.md)
        ) {
            StatCard(
                title = stringResource(id = R.string.analytics_total_streaks),
                value = state.analytics.currentStreak.toString(),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = stringResource(id = R.string.analytics_longest_streak),
                value = state.analytics.longestStreak.toString(),
                modifier = Modifier.weight(1f)
            )
        }

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

        state.error?.let {
            Text(
                text = it,
                color = theme.colors.error,
                style = theme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun AnalyticsPeriod.toLabel(): String {
    return when (this) {
        AnalyticsPeriod.WEEK -> stringResource(R.string.analytics_week)
        AnalyticsPeriod.MONTH -> stringResource(R.string.analytics_month)
        AnalyticsPeriod.YEAR -> stringResource(R.string.analytics_year)
    }
}
