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
import com.mike.hms.houses.bookHouse.HouseBookingScreen
import com.mike.hms.houses.ratingsAndReviews.ReviewsScreen
import com.mike.hms.houses.transactions.TransactionsScreen
import com.mike.hms.model.favorites.FavoriteViewModel
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.profile.ManageAccount
import com.mike.hms.profile.WalletScreen

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

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("dashboard") {
            DashboardScreen(context, houses, navController, houseViewModel, userViewModel)
        }

        composable("book/{houseID}", arguments = listOf(navArgument("houseID") {
            type = NavType.StringType
        })) {
            val houseID = it.arguments?.getString("houseID")
            HouseBookingScreen(context = context, houseId = houseID!!)
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

        composable("houseReviews/{houseID}", arguments = listOf(navArgument("houseID") {
            type = NavType.StringType
        })) {
            val houseID = it.arguments?.getString("houseID")
            HouseReviewsScreen(houseID!!, navController, context)
        }

        composable("houseGallery") {
            HouseGallery(navController, context)
        }

        composable("bookHouse/{houseID}", arguments = listOf(navArgument("houseID") {
            type = NavType.StringType
        })) {
            val houseID = it.arguments?.getString("houseID")
            HouseBookingScreen(context = context, houseId = houseID!!)
        }

        composable("addHouse") {
            HouseForm(context)
        }

        composable("manageAccount") {
            ManageAccount(userViewModel, context, navController)
        }

        composable("transaction") {
            TransactionsScreen(navController = navController, context = context)
        }

        composable("reviews") {
            ReviewsScreen(navController)
        }

        composable("wallet"){
            WalletScreen(context, navController)
        }

        composable("houseGallery/{houseID}", arguments = listOf(navArgument("houseID") {
            type = NavType.StringType
        })) { backStackEntry ->
            val houseID = backStackEntry.arguments?.getString("houseID")
            FullScreenGallery(navController, context, houseID!!)
        }
    }

}