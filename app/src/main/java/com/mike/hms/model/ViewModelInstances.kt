package com.mike.hms.model

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.review.ReviewViewModel
import com.mike.hms.model.roomDatabase.HostelManagementSystemApp
import com.mike.hms.model.userModel.UserViewModel

@Composable
fun getUserViewModel(context: Context): UserViewModel {
    val application = context.applicationContext as HostelManagementSystemApp
    val userRepository = application.userRepository
    return viewModel(factory = UserViewModel.UserViewModelFactory(userRepository))
}

@Composable
fun getHouseViewModel(context: Context): HouseViewModel {
    val application = context.applicationContext as HostelManagementSystemApp
    val houseRepository = application.houseRepository
    return viewModel(factory = HouseViewModel.HouseViewModelFactory(houseRepository))
}

@Composable
fun getReviewViewModel(context: Context): ReviewViewModel {
    val application = context.applicationContext as HostelManagementSystemApp
    val reviewRepository = application.reviewRepository
    return viewModel(factory = ReviewViewModel.ReviewViewModelFactory(reviewRepository))
}

