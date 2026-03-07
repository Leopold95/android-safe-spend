package com.alexandr.safespend.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.alexandr.safespend.R

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object History : Screen("history")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
    object Onboarding1 : Screen("onboarding1")
    object Onboarding2 : Screen("onboarding2")

    object AddDay : Screen("add_day?dayId={dayId}") {
        const val ARG_DAY_ID = "dayId"
        fun createRoute(dayId: Int? = null): String =
            if (dayId == null) "add_day" else "add_day?dayId=$dayId"
    }

    object DayDetail : Screen("day_detail/{dayId}") {
        const val ARG_DAY_ID = "dayId"
        fun createRoute(dayId: Int) = "day_detail/$dayId"
    }

    object ResilienceCalculator : Screen("resilience_calculator")
}

data class BottomNavItem(
    val route: String,
    @StringRes val labelResId: Int,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        labelResId = R.string.nav_home,
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        route = Screen.History.route,
        labelResId = R.string.nav_history,
        icon = Icons.AutoMirrored.Filled.List
    ),
    BottomNavItem(
        route = Screen.Analytics.route,
        labelResId = R.string.nav_analytics,
        icon = Icons.Default.Analytics
    ),
    BottomNavItem(
        route = Screen.Settings.route,
        labelResId = R.string.nav_settings,
        icon = Icons.Default.Settings
    )
)

val topLevelRoutes = bottomNavItems.map { it.route }.toSet()

class NavigationActions(private val navController: NavController) {

    private fun navigateToTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToHome: () -> Unit = {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    val navigateToHistory: () -> Unit = { navigateToTopLevel(Screen.History.route) }
    val navigateToAnalytics: () -> Unit = { navigateToTopLevel(Screen.Analytics.route) }
    val navigateToSettings: () -> Unit = { navigateToTopLevel(Screen.Settings.route) }

    val navigateToAddDay: () -> Unit = {
        navController.navigate(Screen.AddDay.createRoute())
    }

    val navigateToEditDay: (Int) -> Unit = { dayId ->
        navController.navigate(Screen.AddDay.createRoute(dayId))
    }

    val navigateToDayDetail: (Int) -> Unit = { dayId ->
        navController.navigate(Screen.DayDetail.createRoute(dayId))
    }

    val navigateToResilienceCalculator: () -> Unit = {
        navController.navigate(Screen.ResilienceCalculator.route)
    }

    val navigateToOnboarding: () -> Unit = {
        navController.navigate(Screen.Onboarding1.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    val navigateToOnboarding2: () -> Unit = {
        navController.navigate(Screen.Onboarding2.route)
    }

    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
}
