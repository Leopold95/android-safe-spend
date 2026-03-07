package com.alexandr.safespend.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

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
