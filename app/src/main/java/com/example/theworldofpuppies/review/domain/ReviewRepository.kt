package com.example.theworldofpuppies.review.domain

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface ReviewRepository {
    suspend fun addReview(
        targetType: TargetType,
        subType: Category? = null,
        targetId: String,
        stars: Int,
        description: String
    ): Result<Boolean, NetworkError>

    suspend fun getProductReviews(productId: String)
            : Result<List<Review>, NetworkError>

    suspend fun getOrderReviews(subType: Category)
            : Result<List<Review>, NetworkError>

}