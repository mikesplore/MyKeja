package com.mike.hms.model

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mike.hms.model.bedModel.BedViewModel
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.roomDatabase.HostelManagementSystemApp
import com.mike.hms.model.roomModel.RoomViewModel
import com.mike.hms.model.tenantModel.TenantViewModel

@Composable
fun getTenantViewModel(context: Context): TenantViewModel {
    val application = context.applicationContext as HostelManagementSystemApp
    val tenantRepository = application.tenantRepository
    return viewModel(factory = TenantViewModel.TenantViewModelFactory(tenantRepository))
}

@Composable
fun getHouseViewModel(context: Context): HouseViewModel {
    val application = context.applicationContext as HostelManagementSystemApp
    val houseRepository = application.houseRepository
    return viewModel(factory = HouseViewModel.HouseViewModelFactory(houseRepository))
}

@Composable
fun getRoomViewModel(context: Context): RoomViewModel {
    val application = context.applicationContext as HostelManagementSystemApp
    val roomRepository = application.roomRepository
    return viewModel(factory = RoomViewModel.RoomViewModelFactory(roomRepository))
}

@Composable
fun getBedViewModel(context: Context): BedViewModel {
    val application = context.applicationContext as HostelManagementSystemApp
    val bedRepository = application.bedRepository
    return viewModel(factory = BedViewModel.BedViewModelFactory(bedRepository))
}
