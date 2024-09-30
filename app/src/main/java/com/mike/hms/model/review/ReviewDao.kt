package com.mike.hms.model.review

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReviewDao {

    // Get reviews by a specific user along with user information
    @Query("""
        SELECT u.*, r.* FROM reviews AS r
        INNER JOIN users AS u ON r.userId = u.userId
        WHERE r.userId = :userId
    """)
    fun getUserReviews(userId: String): List<ReviewsWithUserInfo>

    // Get reviews for a specific house along with user information
    @Query("""
        SELECT u.*, r.* FROM reviews AS r
        INNER JOIN users AS u ON r.userId = u.userId
        WHERE r.houseId = :houseId
    """)
    fun getHouseReviews(houseId: String): List<ReviewsWithUserInfo>

    // Delete a review by reviewId
    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteReviewById(reviewId: Int)

    // Delete all reviews by a userId
    @Query("DELETE FROM reviews WHERE userId = :userId")
    suspend fun deleteReviewsByUserId(userId: String)

    // Delete all reviews in the table
    @Query("DELETE FROM reviews")
    suspend fun deleteAllReviews()

    // Get all reviews (basic fetch)
    @Query("SELECT * FROM reviews")
    suspend fun getAllReviews(): List<ReviewEntity>

    // Insert a new review
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)
}
