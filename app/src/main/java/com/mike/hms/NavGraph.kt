package com.mike.hms

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mike.hms.homeScreen.DashboardScreen

@Composable
fun NavGraph(context: Context){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard"){
        composable("dashboard"){
            DashboardScreen(context, navController)
        }
    }

}