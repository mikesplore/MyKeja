package com.mike.hms.model.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val reviewRepository: ReviewRepository) :
    ViewModel() {

    // StateFlow for reviews
    private val _reviews = MutableStateFlow<List<ReviewsWithUserInfo>>(emptyList())
    val reviews: StateFlow<List<ReviewsWithUserInfo>> = _reviews

    // Collect all reviews as a Flow
    val allReviews = reviewRepository.observeReviews()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Insert a review
    fun insertReview(review: ReviewEntity, onResult: (Result<Boolean>) -> Unit) {
        viewModelScope.launch {
            reviewRepository.insertReview(review)
                .collect { result -> onResult(result) }
        }
    }

    // Fetch reviews by userId as a Flow
    fun getReviewsByUserId(userId: String) {
        viewModelScope.launch {
            reviewRepository.getReviewsByUserId(userId)
                .collect { userReviews -> _reviews.value = userReviews }
        }
    }

    // Delete a review
    fun deleteReview(reviewId: Int, onResult: (Result<Boolean>) -> Unit) {
        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId)
                .collect { result -> onResult(result) }
        }
    }
}
