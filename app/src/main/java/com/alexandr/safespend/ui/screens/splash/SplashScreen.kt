package com.alexandr.safespend.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexandr.safespend.R
import com.alexandr.safespend.data.datastore.SettingsDataStore
import com.alexandr.safespend.ui.theme.LocalAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    settingsDataStore: SettingsDataStore = koinInject()
) {
    val theme = LocalAppTheme

    // Animation for fade-in effect
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800),
        label = "splashAlpha"
    )

    LaunchedEffect(Unit) {
        // Simulate initialization:
        // - Initialize local storage (Room database)
        // - Check dependency injection (Koin)
        // - Load user preferences
        // - Prepare initial data
        delay(2000) // Simulate loading time

        // Check if user has completed onboarding
        val hasCompletedOnboarding = settingsDataStore.onboardingCompletedFlow.first()

        if (hasCompletedOnboarding) {
            onNavigateToHome()
        } else {
            onNavigateToOnboarding()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Loading indicator
        CircularProgressIndicator(
            color = theme.colors.primaryAccent,
            modifier = Modifier.size(60.dp),
            strokeWidth = 4.dp
        )

        // App name
        Text(
            text = stringResource(id = R.string.app_name),
            color = theme.colors.textPrimary,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
    }
}

