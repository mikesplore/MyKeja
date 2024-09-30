package com.mike.hms.model.review

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mike.hms.model.tenantModel.TenantEntity

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val houseId: String,
    val rating: Int,
    val reviewText: String,
    val timestamp: String
){
    constructor(userId: String, rating: Int, reviewText: String, timestamp: String, houseId: String) :
            this(0,houseId, userId, rating, reviewText, timestamp)

}


data class ReviewsWithUserInfo(
    val review: ReviewEntity,
    val tenant: TenantEntity
)

