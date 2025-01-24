package com.square.assignment.employees

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.square.assignment.employees.employeelist.EmployeeListView

object Routes {
    const val LIST = "list"
    const val DETAILS = "details/{itemId}"
}


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            EmployeeListView(navController)
        }

        composable("details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            //EmployeeDetailsView(navController, id)
        }
    }
}
