package com.deepak.calendly.presenter.navigation


enum class Screen {
    HOME,
    ADD_TASK,
}
sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object AddTask : NavigationItem("${Screen.ADD_TASK.name}/{date}") {
        fun createRoute(date: String) = "${Screen.ADD_TASK.name}/$date"
    }
}