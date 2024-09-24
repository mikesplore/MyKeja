package com.mike.hms

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mike.hms.dashboard.DashboardScreen
import com.mike.hms.homeScreen.HomeScreen
import com.mike.hms.houses.HouseDetailScreen
import com.mike.hms.houses.Houses

@Composable
fun NavGraph(context: Context){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "houseDetails"){
        composable("dashboard"){
            DashboardScreen(context, navController)
        }

        composable("homeScreen"){
            HomeScreen(navController, context)
        }

        composable("houses"){
            Houses(navController, context)
        }

        composable("houseDetails"){
            HouseDetailScreen()
        }
    }
}