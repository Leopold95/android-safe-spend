package com.alexandr.safespend.ui.screens.resilience

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexandr.safespend.R
import com.alexandr.safespend.ui.components.PrimaryButton
import com.alexandr.safespend.ui.theme.LocalAppTheme

@Composable
fun ResilienceCalculatorScreen(
    onNavigateBack: () -> Unit
) {
    val theme = LocalAppTheme
    var scoreInput by remember { mutableStateOf("") }
    var result by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(theme.dimen.md)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md)
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

        OutlinedTextField(
            value = scoreInput,
            onValueChange = { value ->
                scoreInput = value.filter { it.isDigit() }.take(3)
            },
            label = { Text(stringResource(R.string.resilience_score)) },
            supportingText = { Text(stringResource(R.string.resilience_score_hint)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        PrimaryButton(
            text = stringResource(R.string.resilience_calculate),
            onClick = {
                result = scoreInput.toIntOrNull()?.coerceIn(0, 100) ?: 0
            }
        )

        Text(
            text = stringResource(R.string.resilience_result_format, result),
            style = theme.typography.titleLarge,
            color = theme.colors.primaryAccent
        )
    }
}
