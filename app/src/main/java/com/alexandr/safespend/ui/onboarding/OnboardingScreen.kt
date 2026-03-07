package com.alexandr.safespend.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.alexandr.safespend.R
import com.alexandr.safespend.data.datastore.SettingsDataStore
import com.alexandr.safespend.ui.components.PrimaryButton
import com.alexandr.safespend.ui.theme.LocalAppTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun OnboardingScreen1(
    onNavigateNext: () -> Unit
) {
    val theme = LocalAppTheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(theme.dimen.lg),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.onboarding_title_1),
            style = theme.typography.headlineLarge,
            color = theme.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.onboarding_desc_1),
            style = theme.typography.bodyLarge,
            color = theme.colors.textSecondary,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.onboarding_safe_days_explain),
            style = theme.typography.bodyMedium,
            color = theme.colors.textSecondary,
            textAlign = TextAlign.Center
        )

        PrimaryButton(
            text = stringResource(R.string.onboarding_next),
            onClick = onNavigateNext,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OnboardingScreen2(
    onNavigateFinish: () -> Unit,
    settingsDataStore: SettingsDataStore = koinInject()
) {
    val theme = LocalAppTheme
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(theme.dimen.lg),
        verticalArrangement = Arrangement.spacedBy(theme.dimen.md, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.onboarding_title_2),
            style = theme.typography.headlineLarge,
            color = theme.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.onboarding_desc_2),
            style = theme.typography.bodyLarge,
            color = theme.colors.textSecondary,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.onboarding_manual_input_explain),
            style = theme.typography.bodyMedium,
            color = theme.colors.textSecondary,
            textAlign = TextAlign.Center
        )

        PrimaryButton(
            text = stringResource(R.string.onboarding_finish),
            onClick = {
                coroutineScope.launch {
                    settingsDataStore.setOnboardingCompleted(true)
                    onNavigateFinish()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
