package com.example.theworldofpuppies.review.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError

data class ReviewListState(
    val groomingReviewLoading: Boolean = false,
    val petWalkReviewLoading: Boolean = false,
    val vetReviewLoading: Boolean = false,
    val dogTrainingReviewLoading: Boolean = false,
    val productReviewLoading: Boolean = false,
    val groomingReviews: List<Review> = emptyList(),
    val petWalkReviews: List<Review> = emptyList(),
    val dogTrainingReviews: List<Review> = emptyList(),
    val vetReviews: List<Review> = emptyList(),
    val productReviews: List<Review> = emptyList(),
    val groomingReviewError: NetworkError? = null,
    val petWalkReviewError: NetworkError? = null,
    val vetReviewError: NetworkError? = null,
    val dogTrainingReviewError: NetworkError? = null,
    val productReviewError: NetworkError? = null
)
