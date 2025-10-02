package com.example.theworldofpuppies.review.domain

import com.example.theworldofpuppies.booking.core.domain.Category

data class ReviewUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val productReviews: List<Review> = emptyList(),
    val orderReviews: List<Review> = emptyList(),
    val stars: Float = 0f,
    val description: String = "",
    val targetId: String = "",
    val targetType: TargetType = TargetType.NULL,
    val subType: Category? = null,
)
