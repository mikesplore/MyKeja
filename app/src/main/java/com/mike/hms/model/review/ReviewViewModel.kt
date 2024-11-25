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


    // Insert a review
    fun insertReview(review: ReviewEntity, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            reviewRepository.insertReview(review)
                .collect { result -> onResult(result.isSuccess) }
        }
    }

    // Fetch reviews by userId as a Flow
    fun getReviewsByUserId(userId: String) {
        viewModelScope.launch {
            reviewRepository.getReviewsByUserId(userId)
                .collect { userReviews -> _reviews.value = userReviews }
        }
    }

    //Fetch all reviews by house ID
    fun getReviewsByHouseId(houseId: String) {
        viewModelScope.launch {
            reviewRepository.getReviewsByHouseId(houseId)
                .collect { reviews -> _reviews.value = reviews }
        }
    }


    // Delete a review
    fun deleteReview(reviewId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId)
                .collect { result -> onResult(result.isSuccess) }
        }
    }
}
