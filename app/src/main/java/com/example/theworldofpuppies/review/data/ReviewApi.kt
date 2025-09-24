package com.example.theworldofpuppies.review.data

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.review.data.dto.ReviewDto
import com.example.theworldofpuppies.review.data.request.AddReviewRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ReviewApi(private val httpClient: HttpClient) {

    suspend fun addReview(request: AddReviewRequest, token: String)
            : Result<ApiResponse<Unit>, NetworkError> {
        return safeCall {
            httpClient.post(constructUrl("review/add")) {
                header("Authorization", token)
                setBody(request)
            }
        }
    }

    suspend fun getProductReviews(productId: String)
            : Result<ApiResponse<List<ReviewDto>>, NetworkError> {
        return safeCall {
            httpClient.get(constructUrl("review/product")) {
                parameter("productId", productId)
            }
        }
    }

    suspend fun getOrderReviews(subType: Category)
            : Result<ApiResponse<List<ReviewDto>>, NetworkError> {
        return safeCall {
            httpClient.get(constructUrl("review/booking")) {
                parameter("subType", subType)
            }
        }
    }
}