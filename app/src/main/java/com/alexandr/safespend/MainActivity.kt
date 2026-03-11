package com.alexandr.safespend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alexandr.safespend.ui.navigation.BottomNavBar
import com.alexandr.safespend.ui.navigation.NavigationActions
import com.alexandr.safespend.ui.navigation.Screen
import com.alexandr.safespend.ui.navigation.topLevelRoutes
import com.alexandr.safespend.ui.screens.addday.AddDayScreen
import com.alexandr.safespend.ui.screens.analytics.AnalyticsScreen
import com.alexandr.safespend.ui.screens.daydetail.DayDetailScreen
import com.alexandr.safespend.ui.screens.history.HistoryScreen
import com.alexandr.safespend.ui.screens.home.HomeScreen
import com.alexandr.safespend.ui.screens.onboarding.OnboardingScreen1
import com.alexandr.safespend.ui.screens.onboarding.OnboardingScreen2
import com.alexandr.safespend.ui.screens.resilience.ResilienceCalculatorScreen
import com.alexandr.safespend.ui.screens.settings.SettingsScreen
import com.alexandr.safespend.ui.screens.splash.SplashScreen
import com.alexandr.safespend.ui.theme.AndroidsafespendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidsafespendTheme {
                SafeSpendNavHost()
            }
        }
    }
}

@Composable
fun SafeSpendNavHost() {
    val navController = rememberNavController()
    val navActions = NavigationActions(navController)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in topLevelRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentDestination = backStackEntry?.destination,
                    onNavigateToRoute = { route ->
                        when (route) {
                            Screen.Home.route -> navActions.navigateToHome()
                            Screen.History.route -> navActions.navigateToHistory()
                            Screen.Analytics.route -> navActions.navigateToAnalytics()
                            Screen.Settings.route -> navActions.navigateToSettings()
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onNavigateToOnboarding = navActions.navigateToOnboarding,
                        onNavigateToHome = navActions.navigateToHome
                    )
                }

                composable(Screen.Onboarding1.route) {
                    OnboardingScreen1(onNavigateNext = navActions.navigateToOnboarding2)
                }

                composable(Screen.Onboarding2.route) {
                    OnboardingScreen2(onNavigateFinish = navActions.navigateToHome)
                }

                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToAddDay = navActions.navigateToAddDay,
                        onNavigateToHistory = navActions.navigateToHistory,
                        onNavigateToAnalytics = navActions.navigateToAnalytics,
                        onNavigateToCalculator = navActions.navigateToResilienceCalculator,
                        onNavigateToSettings = navActions.navigateToSettings
                    )
                }

                composable(Screen.History.route) {
                    HistoryScreen(
                        onNavigateToDayDetail = navActions.navigateToDayDetail,
                        onNavigateToAddDay = navActions.navigateToAddDay
                    )
                }

                composable(Screen.Analytics.route) {
                    AnalyticsScreen()
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(onNavigateHomeAfterReset = navActions.navigateToHome)
                }

                composable(
                    route = Screen.AddDay.route,
                    arguments = listOf(
                        navArgument(Screen.AddDay.ARG_DAY_ID) {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) { backStackEntry ->
                    val dayIdArg = backStackEntry.arguments?.getInt(Screen.AddDay.ARG_DAY_ID, -1) ?: -1
                    val editDayId = if (dayIdArg > 0) dayIdArg else null
                    AddDayScreen(
                        editDayId = editDayId,
                        onNavigateBack = navActions.navigateBack
                    )
                }

                composable(
                    route = Screen.DayDetail.route,
                    arguments = listOf(navArgument(Screen.DayDetail.ARG_DAY_ID) { type = NavType.IntType })
                ) { backStackEntry ->
                    val dayId = backStackEntry.arguments?.getInt(Screen.DayDetail.ARG_DAY_ID) ?: 0
                    DayDetailScreen(
                        dayId = dayId,
                        onNavigateBack = navActions.navigateBack,
                        onNavigateToEditDay = navActions.navigateToEditDay
                    )
                }

                composable(Screen.ResilienceCalculator.route) {
                    ResilienceCalculatorScreen(
                        onNavigateBack = navActions.navigateBack
                    )
                }
            }
        }
    }
}
