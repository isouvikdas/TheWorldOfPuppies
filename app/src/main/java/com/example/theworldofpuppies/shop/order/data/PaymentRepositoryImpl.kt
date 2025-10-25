package com.example.theworldofpuppies.shop.order.data

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.order.data.requests.PaymentRequest
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest
import com.example.theworldofpuppies.shop.order.data.response.PaymentResponse
import com.example.theworldofpuppies.shop.order.domain.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaymentRepositoryImpl(
    private val paymentApi: PaymentApi
) : PaymentRepository {
    override suspend fun createPaymentOrder(paymentRequest: PaymentRequest): Result<PaymentResponse, NetworkError> {
        return withContext(Dispatchers.IO) {
            when (val result = paymentApi.createPaymentOrder(paymentRequest)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success || response.data == null) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        Result.Success(response.data)
                    }
                }

                is Result.Error -> {
                    Result.Error(NetworkError.UNKNOWN)
                }
            }
        }
    }

    override suspend fun verifyPaymentOrder(paymentVerificationRequest: PaymentVerificationRequest)
            : Result<Boolean, NetworkError> {
        return withContext(Dispatchers.IO) {
            when (val result = paymentApi.verifyPayment(paymentVerificationRequest)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        Result.Success(true)
                    }
                }

                is Result.Error -> {
                    Result.Error(NetworkError.UNKNOWN)
                }
            }
        }
    }
}