package com.mike.hms.model.review

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReviewDao {

    @Query("SELECT * FROM reviews WHERE userId = :userId")
    fun getUserReviews(userId: String): List<ReviewsWithUserInfo>

    @Query("SELECT * FROM reviews WHERE houseId = :houseId")
    fun getHouseReviews(houseId: Int): List<ReviewsWithUserInfo>

    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteReviewById(reviewId: Int)

    @Query("DELETE FROM reviews WHERE userId = :userId")
    suspend fun deleteReviewsByUserId(userId: String)

    @Query("DELETE FROM reviews")
    suspend fun deleteAllReviews()

    @Query("SELECT * FROM reviews")
    suspend fun getAllReviews(): List<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Query("SELECT u.*, r.userId, r.houseId, r.rating, r.reviewText, r.timestamp FROM reviews AS r JOIN tenantTable AS u ON r.userId = u.tenantID WHERE r.houseId = :houseId")
    fun getReviewsWithUserInfo(houseId: String): List<ReviewsWithUserInfo>


}