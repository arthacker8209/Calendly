package com.deepak.calendly.presenter.navigation


import AddTaskScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deepak.calendly.presenter.features.task.CalendarScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            CalendarScreen(navController)
        }
        composable(
            route = NavigationItem.AddTask.route,
        ) { backStackEntry ->
            val dateString = backStackEntry.arguments?.getString("date")
            AddTaskScreen(navController, date = dateString)
        }
    }
}
