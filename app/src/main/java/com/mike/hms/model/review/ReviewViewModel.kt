package com.mike.hms.model.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val reviewRepository: ReviewRepository): ViewModel() {
    private val _reviews = MutableLiveData<List<ReviewsWithUserInfo>>()
    val reviews: LiveData<List<ReviewsWithUserInfo>> = _reviews

    fun insertReview(review: ReviewEntity, onSuccess: (Boolean) -> Unit) {
        reviewRepository.insertReview(review) { success ->
            onSuccess(success)
        }
    }

    fun getReviewsByUserId(userId: String) {
        reviewRepository.getReviewsByUserId(userId) { reviews ->
            this._reviews.value = reviews
        }
    }

    fun deleteReview(reviewId: Int, onSuccess: (Boolean) -> Unit) {
        reviewRepository.deleteReview(reviewId) { success ->
            onSuccess(success)
        }

    }

    fun getAllReviews() {
        reviewRepository.getAllReviews()
    }

}