package com.alexandr.safespend.ui.screens.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexandr.safespend.R
import com.alexandr.safespend.data.model.ResilienceChartPoint
import com.alexandr.safespend.ui.components.StatCard
import com.alexandr.safespend.ui.theme.LocalAppTheme
import com.alexandr.safespend.utils.DateUtils.formatIsoDateShort
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = koinViewModel()
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(theme.dimen.md)
        ) {
            StatCard(
                title = stringResource(id = R.string.analytics_resilience_average),
                value = state.analytics.averageResilienceScore?.toString() ?: "—",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = stringResource(id = R.string.analytics_resilience_latest),
                value = state.analytics.latestResilienceScore?.toString() ?: "—",
                modifier = Modifier.weight(1f)
            )
        }

        ResilienceLineChart(points = state.analytics.resiliencePoints)

        AnalyticsRecommendation(
            averageResilienceScore = state.analytics.averageResilienceScore
        )

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
private fun ResilienceLineChart(points: List<ResilienceChartPoint>) {
    val theme = LocalAppTheme
    val borderColor = theme.colors.border
    val pointColor = theme.colors.secondaryAccent
    val lineColor = theme.colors.primaryAccent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = theme.colors.surface,
                shape = RoundedCornerShape(theme.dimen.lg)
            )
            .padding(theme.dimen.lg),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
    ) {
        Text(
            text = stringResource(R.string.analytics_resilience_chart),
            style = theme.typography.titleLarge,
            color = theme.colors.textPrimary
        )

        if (points.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.analytics_no_chart_data),
                    color = theme.colors.textSecondary,
                    style = theme.typography.bodyMedium
                )
            }
            return
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val leftPadding = 24.dp.toPx()
            val topPadding = 12.dp.toPx()
            val bottomPadding = 20.dp.toPx()
            val chartWidth = size.width - leftPadding
            val chartHeight = size.height - topPadding - bottomPadding
            val safeChartWidth = if (chartWidth > 0f) chartWidth else size.width
            val safeChartHeight = if (chartHeight > 0f) chartHeight else size.height
            val stepX = if (points.size > 1) safeChartWidth / (points.size - 1) else 0f

            drawLine(
                color = borderColor,
                start = Offset(leftPadding, topPadding + safeChartHeight),
                end = Offset(leftPadding + safeChartWidth, topPadding + safeChartHeight),
                strokeWidth = 2f
            )

            val path = Path()
            points.forEachIndexed { index, point ->
                val x = leftPadding + (index * stepX)
                val yRatio = point.score / 100f
                val y = topPadding + safeChartHeight - (safeChartHeight * yRatio)
                val offset = Offset(x, y)
                if (index == 0) {
                    path.moveTo(offset.x, offset.y)
                } else {
                    path.lineTo(offset.x, offset.y)
                }
                drawCircle(
                    color = pointColor,
                    radius = 6f,
                    center = offset
                )
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val first = points.firstOrNull()?.date?.let(::formatIsoDateShort).orEmpty()
            val middle = points.getOrNull(points.lastIndex / 2)?.date?.let(::formatIsoDateShort).orEmpty()
            val last = points.lastOrNull()?.date?.let(::formatIsoDateShort).orEmpty()
            listOf(first, middle, last).forEach { label ->
                Text(
                    text = label,
                    style = theme.typography.bodySmall,
                    color = theme.colors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun AnalyticsRecommendation(averageResilienceScore: Int?) {
    val theme = LocalAppTheme
    val messageRes = when {
        averageResilienceScore == null -> R.string.analytics_recommendation_low
        averageResilienceScore >= 75 -> R.string.analytics_recommendation_high
        averageResilienceScore >= 45 -> R.string.analytics_recommendation_medium
        else -> R.string.analytics_recommendation_low
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = theme.colors.surface,
                shape = RoundedCornerShape(theme.dimen.lg)
            )
            .padding(theme.dimen.lg),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.sm)
    ) {
        Text(
            text = stringResource(R.string.analytics_recommendation_title),
            style = theme.typography.titleMedium,
            color = theme.colors.textPrimary
        )
        Text(
            text = stringResource(messageRes),
            style = theme.typography.bodyMedium,
            color = theme.colors.textSecondary
        )
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
