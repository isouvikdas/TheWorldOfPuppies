package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.order.data.requests.PaymentRequest
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest
import com.example.theworldofpuppies.shop.order.data.response.PaymentResponse

interface PaymentRepository {
    suspend fun createPaymentOrder(paymentRequest: PaymentRequest): Result<PaymentResponse, NetworkError>
    suspend fun verifyPaymentOrder(paymentVerificationRequest: PaymentVerificationRequest): Result<Boolean, NetworkError>
}