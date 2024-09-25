package com.mike.hms

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mike.hms.dashboard.DashboardScreen
import com.mike.hms.homeScreen.HomeScreen
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.houses.FullScreenGallery
import com.mike.hms.houses.HouseDetailScreen
import com.mike.hms.houses.HouseGallery
import com.mike.hms.houses.HouseReviewsScreen
import com.mike.hms.houses.Houses

@Composable
fun NavGraph(context: Context){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeScreen"){
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
            HouseDetailScreen(navController)
        }

        composable("houseReviews"){
            HouseReviewsScreen(navController)
        }

        composable("houseGallery"){
            HouseGallery(navController)
        }

        composable("fullScreenGallery/{initialPage}") {
            val initialPage = it.arguments?.getString("initialPage")?.toIntOrNull() ?:
            throw IllegalArgumentException("Initial page argument not found")
            FullScreenGallery(
                initialPage = initialPage,
                images = houseTypes[0].houseImageLink,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}