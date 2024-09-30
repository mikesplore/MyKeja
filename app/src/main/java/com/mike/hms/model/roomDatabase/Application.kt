package com.mike.hms.model.roomDatabase

import android.app.Application
import com.mike.hms.model.houseModel.HouseRepository
import com.mike.hms.model.review.ReviewRepository
import com.mike.hms.model.userModel.UserRepository

class HostelManagementSystemApp () : Application() {
    val database by lazy { HMSDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val houseRepository by lazy { HouseRepository(database.houseDao()) }
    val reviewRepository by lazy { ReviewRepository(database.reviewDao()) }



}