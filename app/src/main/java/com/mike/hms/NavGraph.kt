package com.mike.hms

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mike.hms.dashboard.DashboardScreen
import com.mike.hms.homeScreen.HomeScreen
import com.mike.hms.houses.FullScreenGallery
import com.mike.hms.houses.HouseGallery
import com.mike.hms.houses.HouseInfoScreen
import com.mike.hms.houses.HouseReviewsScreen
import com.mike.hms.houses.Houses
import com.mike.hms.houses.addOrEditHouse.HouseForm
import com.mike.hms.houses.bookHouse.BookingInfoScreen

@Composable
fun NavGraph(context: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("dashboard") {
            DashboardScreen(context, navController)
        }

        composable("homeScreen") {
            HomeScreen(navController, context)
        }

        composable("houses") {
            Houses(navController, context)
        }

        composable(
            "houseDetails/{houseID}", arguments = listOf(navArgument("houseID") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val houseID = backStackEntry.arguments?.getString("houseID")
            HouseInfoScreen(navController, context, houseID!!)

        }

        composable("houseReviews") {
            HouseReviewsScreen(navController, context)
        }

        composable("houseGallery") {
            HouseGallery(navController, context)
        }

        composable("bookHouse/{houseID}", arguments = listOf(navArgument("houseID") {
            type = NavType.StringType
        })) {
            val houseID = it.arguments?.getString("houseID")
            BookingInfoScreen(context, houseID!!)
        }

        composable("addHouse") {
            HouseForm(context)
        }

        composable("houseGallery/{houseID}", arguments = listOf(navArgument("houseID") {
            type = NavType.StringType
        })) { backStackEntry ->
            val houseID = backStackEntry.arguments?.getString("houseID")
            FullScreenGallery(navController, context, houseID!!)
        }
    }

}