package com.mike.hms.model.review

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    // Get reviews by a specific user along with user information as a Flow
    @Query("""
        SELECT u.*, r.* FROM reviews AS r
        INNER JOIN users AS u ON r.userId = u.userId
        WHERE r.userId = :userId
    """)
    fun getUserReviewsAsFlow(userId: String): Flow<List<ReviewsWithUserInfo>>

    // Get reviews for a specific house along with user information as a Flow
    @Query("""
        SELECT u.*, r.* FROM reviews AS r
        INNER JOIN users AS u ON r.userId = u.userId
        WHERE r.houseId = :houseId
    """)
    fun getHouseReviewsAsFlow(houseId: String): Flow<List<ReviewsWithUserInfo>>

    // Delete a review by reviewId
    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteReviewById(reviewId: String)

    // Delete all reviews by a userId
    @Query("DELETE FROM reviews WHERE userId = :userId")
    suspend fun deleteReviewsByUserId(userId: String)

    // Delete all reviews by a houseId
    @Query("DELETE FROM reviews WHERE houseId = :houseId")
    suspend fun deleteReviewsByHouseId(houseId: String)

    // Delete all reviews in the table
    @Query("DELETE FROM reviews")
    suspend fun deleteAllReviews()

    // Get all reviews as a Flow
    @Query("SELECT * FROM reviews")
    fun getAllReviewsAsFlow(): Flow<List<ReviewEntity>>

    // Insert a new review
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)
}
