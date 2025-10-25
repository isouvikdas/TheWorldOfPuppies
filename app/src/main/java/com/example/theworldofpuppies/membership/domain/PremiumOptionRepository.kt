package com.example.theworldofpuppies.membership.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest

interface PremiumOptionRepository {
    suspend fun getPremiumOptions(): Result<List<PremiumOption>, NetworkError>

    suspend fun buyPremium(premiumOptionId: String)
            : Result<PremiumOptionOrder, NetworkError>

    suspend fun verifyPayment(paymentVerificationRequest: PaymentVerificationRequest)
            : Result<Boolean, NetworkError>

    suspend fun getPremiumOptionOrder(premiumOptionOrderId: String)
            : Result<PremiumOptionOrder, NetworkError>
}