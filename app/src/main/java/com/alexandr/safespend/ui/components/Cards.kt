package com.alexandr.safespend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.alexandr.safespend.R
import com.alexandr.safespend.ui.theme.LocalAppTheme

@Composable
fun StreakCard(
    streak: Int,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppTheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = theme.colors.primaryAccent,
                shape = RoundedCornerShape(theme.dimen.lg)
            )
            .padding(theme.dimen.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
    ) {
        Text(
            text = stringResource(R.string.home_current_streak),
            style = theme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
            color = Color.White.copy(alpha = 0.9f)
        )
        Text(
            text = streak.toString(),
            style = theme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.home_days),
            style = theme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null
) {
    val theme = LocalAppTheme
    val bgColor = backgroundColor ?: theme.colors.surface

    Column(
        modifier = modifier
            .background(
                color = bgColor,
                shape = RoundedCornerShape(theme.dimen.lg)
            )
            .padding(theme.dimen.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
    ) {
        Text(
            text = title,
            style = theme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = theme.colors.textSecondary,
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            style = theme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = theme.colors.textPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String = "",
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    val theme = LocalAppTheme
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = isEnabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = theme.dimen.md, horizontal = theme.dimen.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(theme.dimen.sm)
        ) {
            Text(
                text = title,
                style = theme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = theme.colors.textPrimary
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = theme.typography.bodySmall,
                    color = theme.colors.textSecondary
                )
            }
        }
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = theme.colors.textSecondary,
                modifier = Modifier.padding(start = theme.dimen.md)
            )
        }
    }
}

@Composable
fun LoadingCard(
    modifier: Modifier = Modifier
) {
    val theme = LocalAppTheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = theme.colors.surface,
                shape = RoundedCornerShape(theme.dimen.lg)
            )
            .padding(theme.dimen.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(
            color = theme.colors.primaryAccent,
            modifier = Modifier.padding(theme.dimen.lg)
        )
    }
}
