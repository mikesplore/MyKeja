package com.mike.hms.model.review

import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewRepository(private val reviewDao: ReviewDao) {

    private val viewmodelScope = CoroutineScope(Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance().reference.child("Reviews")

    init {
        // Initialize listeners to keep Firebase and Room in sync
        syncReviewsFromFirebase()
    }

    // Insert a review into both Firebase and Room
    fun insertReview(review: ReviewEntity, onSuccess: (Boolean) -> Unit) {
        val reviewId = review.id.toString()
        viewmodelScope.launch {
            try {
                reviewDao.insertReview(review)
                database.child(reviewId).setValue(review)
                    .addOnSuccessListener {
                        onSuccess(true)
                    }
                    .addOnFailureListener {
                        onSuccess(false)
                    }
            } catch (e: Exception) {
                onSuccess(false)
            }
        }
    }

    // Retrieve reviews by user ID from Room (after initial sync from Firebase)
    fun getReviewsByUserId(userId: String, onResult: (List<ReviewsWithUserInfo>) -> Unit) {
        viewmodelScope.launch {
            val reviews = reviewDao.getUserReviews(userId)
            onResult(reviews)
        }
    }

    // Get all reviews from Firebase, which will update Room accordingly
    fun getAllReviews() {
        viewmodelScope.launch {
            val reviews = reviewDao.getAllReviews()
            reviews.forEach { review ->
                reviewDao.insertReview(review)
            }
        }
    }

    // Get a specific review by ID from Room
    fun getReviewById(houseId: String, onResult: (List<ReviewsWithUserInfo>) -> Unit) {
        viewmodelScope.launch {
            val review = reviewDao.getHouseReviews(houseId)
            onResult(review)
        }
    }

    // Delete a review from Firebase and Room
    fun deleteReview(reviewId: Int, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            try {
                reviewDao.deleteReviewById(reviewId)
                database.child(reviewId.toString()).removeValue()
                    .addOnSuccessListener {
                        onSuccess(true)
                    }
                    .addOnFailureListener {
                        onSuccess(false)
                    }
            } catch (e: Exception) {
                onSuccess(false)
            }
        }
    }


    // Sync reviews from Firebase to Room whenever there's a change in Firebase
    private fun syncReviewsFromFirebase() {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ReviewEntity::class.java)?.let { review ->
                    viewmodelScope.launch {
                        reviewDao.insertReview(review) // Insert into Room
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ReviewEntity::class.java)?.let { review ->
                    viewmodelScope.launch {
                        reviewDao.insertReview(review) // Update in Room
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(ReviewEntity::class.java)?.let { review ->
                    viewmodelScope.launch {
                        reviewDao.deleteReviewById(review.id) // Delete from Room
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Not typically needed for this use case
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.e("ReviewRepository", "Firebase child event listener cancelled with error: ${error.message}")
            }
        })
    }
}
