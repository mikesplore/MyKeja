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
import com.mike.hms.houses.statement.StatementsScreen
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.favorites.FavoriteViewModel
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.profile.ManageAccount
import com.mike.hms.viewmodel.StatementViewModel

@Composable
fun NavGraph(
    houseViewModel: HouseViewModel,
    houses: List<HouseEntity>,
    favoriteViewModel: FavoriteViewModel,
    userViewModel: UserViewModel,
    creditCardViewModel: CreditCardViewModel,
    context: Context
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "statement") {
        composable("dashboard") {
            DashboardScreen(context, houses, navController, houseViewModel, userViewModel)
        }

        composable("homeScreen") {
            HomeScreen(
                houses,
                navController,
                context,
                favoriteViewModel,
                houseViewModel,
                userViewModel,
                creditCardViewModel
            )
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

        composable("manageAccount"){
            ManageAccount(userViewModel, context, navController)
        }

        composable("statement"){
            StatementsScreen(navController = navController)
        }

        composable("houseGallery/{houseID}", arguments = listOf(navArgument("houseID") {
            type = NavType.StringType
        })) { backStackEntry ->
            val houseID = backStackEntry.arguments?.getString("houseID")
            FullScreenGallery(navController, context, houseID!!)
        }
    }

}