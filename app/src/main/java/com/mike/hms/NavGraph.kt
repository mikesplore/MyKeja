package com.mike.hms

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mike.hms.dashboard.DashboardScreen
import com.mike.hms.homeScreen.HomeScreen
import com.mike.hms.houses.bookHouse.BookingInfoScreen
import com.mike.hms.houses.FullScreenGallery
import com.mike.hms.houses.HouseDetailScreen
import com.mike.hms.houses.addHouse.HouseForm
import com.mike.hms.houses.HouseGallery
import com.mike.hms.houses.HouseReviewsScreen
import com.mike.hms.houses.Houses

@Composable
fun NavGraph(context: Context){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "addHouse"){
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
            HouseDetailScreen(navController,context)
        }

        composable("houseReviews"){
            HouseReviewsScreen(navController,context)
        }

        composable("houseGallery"){
            HouseGallery(navController,context)
        }

        composable("booking"){
            BookingInfoScreen(context)
        }

        composable("addHouse") {
            HouseForm(context)
        }

        composable("fullScreenGallery/{initialPage}") {
            FullScreenGallery(navController,context)
        }
    }
}