package com.mac.cozyfarm.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Shop : Screen("shop")
    object Stats : Screen("stats")
}
