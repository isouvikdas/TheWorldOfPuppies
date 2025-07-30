package com.example.theworldofpuppies.shop.order.data

import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.order.data.requests.PaymentRequest
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest
import com.example.theworldofpuppies.shop.order.data.response.PaymentResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PaymentApi(
    private val httpClient: HttpClient
) {

    suspend fun createPaymentOrder(paymentRequest: PaymentRequest) : Result<ApiResponse<PaymentResponse>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("payments/create")
            ) {
                setBody(paymentRequest)
            }
        }
    }
    suspend fun verifyPayment(paymentVerificationRequest: PaymentVerificationRequest) : Result<ApiResponse<Boolean>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("payments/verify")
            ) {
                setBody(paymentVerificationRequest)

            }
        }
    }

}