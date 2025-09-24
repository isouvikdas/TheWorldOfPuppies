package com.example.theworldofpuppies.review.data.request

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.review.data.ReviewApi
import com.example.theworldofpuppies.review.data.mappers.toReview
import com.example.theworldofpuppies.review.domain.Review
import com.example.theworldofpuppies.review.domain.ReviewRepository
import com.example.theworldofpuppies.review.domain.TargetType

class ReviewRepositoryImpl(
    private val userRepository: UserRepository,
    private val api: ReviewApi
) : ReviewRepository{

    override suspend fun addReview(
        targetType: TargetType,
        subType: Category?,
        targetId: String,
        stars: Int,
        description: String
    ): Result<Boolean, NetworkError> {

        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        val request = AddReviewRequest(
            targetType = targetType,
            subType = subType,
            targetId = targetId,
            stars = stars,
            description = description
        )
        return when (val result = api.addReview(request, token)) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success -> Result.Success(true)
                    else -> Result.Error(NetworkError.SERVER_ERROR)
                }
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getProductReviews(productId: String)
            : Result<List<Review>, NetworkError> {
        return when (val result = api.getProductReviews(productId)) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success && !response.data.isNullOrEmpty() -> {
                        Result.Success(response.data.map { it.toReview() })
                    }
                    response.success && response.data!!.isEmpty() -> {
                        Result.Success(emptyList())
                    }
                    else -> {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }
                }
            }
            is Result.Error -> {
                Result.Error(result.error)
            }
        }

    }

    override suspend fun getOrderReviews(subType: Category)
            : Result<List<Review>, NetworkError> {
        return when (val result = api.getOrderReviews(subType)) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success && !response.data.isNullOrEmpty() -> {
                        Result.Success(response.data.map { it.toReview() })
                    }
                    response.success && response.data!!.isEmpty() -> {
                        Result.Success(emptyList())
                    }
                    else -> {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }
                }
            }
            is Result.Error -> {
                Result.Error(result.error)
            }
        }

    }
}