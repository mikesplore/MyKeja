package com.mike.hms.model.roomDatabase

import android.app.Application
import com.mike.hms.model.bedModel.BedRepository
import com.mike.hms.model.houseModel.HouseRepository
import com.mike.hms.model.roomModel.RoomRepository
import com.mike.hms.model.tenantModel.TenantRepository

class HostelManagementSystemApp () : Application() {
    val database by lazy { HMSDatabase.getDatabase(this) }
    val tenantRepository by lazy { TenantRepository(database.tenantDao()) }
    val houseRepository by lazy { HouseRepository(database.houseDao()) }
    val roomRepository by lazy { RoomRepository(database.roomDao()) }
    val bedRepository by lazy { BedRepository(database.bedDao()) }

}