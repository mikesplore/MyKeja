package com.mike.hms.model.review

import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class ReviewRepository(private val reviewDao: ReviewDao) {

    private val database = FirebaseDatabase.getInstance().reference.child("Reviews")
    private val reviewFlow = MutableSharedFlow<List<ReviewEntity>>(replay = 1)

    init {
        // Initialize listeners to keep Firebase and Room in sync
        syncReviewsFromFirebase()
    }

    // Insert a review into both Firebase and Room
    fun insertReview(review: ReviewEntity): Flow<Result<Boolean>> = callbackFlow {
        val reviewId = review.id.toString()
        try {
            reviewDao.insertReview(review)
            database.child(reviewId).setValue(review)
                .addOnSuccessListener {
                    trySend(Result.success(true))
                    close()
                }
                .addOnFailureListener { exception ->
                    trySend(Result.failure(exception))
                    close(exception)
                }
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close(e)
        }
        awaitClose { /* Cleanup if necessary */ }
    }.flowOn(Dispatchers.IO)


    // Retrieve reviews by user ID as a Flow
    fun getReviewsByUserId(userId: String): Flow<List<ReviewsWithUserInfo>> {
        return reviewDao.getUserReviewsAsFlow(userId).flowOn(Dispatchers.IO)
    }

    // Retrieve reviews by house ID as a Flow
    fun getReviewsByHouseId(houseId: String): Flow<List<ReviewsWithUserInfo>> {
        return reviewDao.getHouseReviewsAsFlow(houseId).flowOn(Dispatchers.IO)
    }


    // Delete a review from Firebase and Room
    fun deleteReview(reviewId: Int): Flow<Result<Boolean>> = callbackFlow {
        try {
            reviewDao.deleteReviewById(reviewId)
            database.child(reviewId.toString()).removeValue()
                .addOnSuccessListener {
                    trySend(Result.success(true))
                    close()
                }
                .addOnFailureListener { exception ->
                    trySend(Result.failure(exception))
                    close(exception)
                }
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close(e)
        }
        awaitClose { /* Cleanup if necessary */ }
    }.flowOn(Dispatchers.IO)


    // Sync reviews from Firebase to Room and emit them as Flows
    private fun syncReviewsFromFirebase() {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ReviewEntity::class.java)?.let { review ->
                    CoroutineScope(Dispatchers.IO).launch {
                        reviewDao.insertReview(review) // Insert into Room
                        emitReviewsFromRoom() // Emit updated list from Room
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ReviewEntity::class.java)?.let { review ->
                    CoroutineScope(Dispatchers.IO).launch {
                        reviewDao.insertReview(review) // Update in Room
                        emitReviewsFromRoom() // Emit updated list from Room
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(ReviewEntity::class.java)?.let { review ->
                    CoroutineScope(Dispatchers.IO).launch {
                        reviewDao.deleteReviewById(review.id.toInt()) // Delete from Room
                        emitReviewsFromRoom() // Emit updated list from Room
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Not typically needed for this use case
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ReviewRepository", "Firebase child event listener cancelled with error: ${error.message}")
            }
        })
    }

    // Emit reviews from Room into the Flow
    private suspend fun emitReviewsFromRoom() {
        reviewDao.getAllReviewsAsFlow()
            .collect { reviews ->
                reviewFlow.emit(reviews) // Emit the collected List into SharedFlow
            }
    }


    // Expose the Flow of reviews
    fun observeReviews(): Flow<List<ReviewEntity>> = reviewFlow.asSharedFlow()
}
