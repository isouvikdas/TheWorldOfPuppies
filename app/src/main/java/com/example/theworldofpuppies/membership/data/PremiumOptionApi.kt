package com.example.theworldofpuppies.membership.data

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.membership.data.dto.PremiumOptionOrderDto
import com.example.theworldofpuppies.membership.domain.PremiumOption
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PremiumOptionApi(
    private val httpClient: HttpClient
) {

    suspend fun getPremiumOptions()
            : Result<ApiResponse<List<PremiumOption>>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("auth/premium-option")
            )
        }
    }

    suspend fun buyPremium(token: String, premiumOptionId: String)
            : Result<ApiResponse<PremiumOptionOrderDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                constructUrl("auth/premium-option/buy")
            ) {
                header("Authorization", token)
                parameter("premiumOptionId", premiumOptionId)
            }
        }
    }

    suspend fun verifyPayment(paymentVerificationRequest: PaymentVerificationRequest)
            : Result<ApiResponse<Boolean>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("payments/verify")
            ) {
                setBody(paymentVerificationRequest)
            }
        }
    }

    suspend fun getPremiumOptionOrder(premiumOptionOrderId: String)
            : Result<ApiResponse<PremiumOptionOrderDto>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("auth/premium-option/order")
            ) {
                parameter("premiumOptionOrderId", premiumOptionOrderId)
            }
        }
    }
}