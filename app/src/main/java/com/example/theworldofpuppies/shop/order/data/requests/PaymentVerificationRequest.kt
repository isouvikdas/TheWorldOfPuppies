package com.example.theworldofpuppies.shop.order.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class PaymentVerificationRequest(
    val razorpayOrderId: String,
    val orderId: String,
    val publicOrderId: String,
    val paymentId: String,
    val signature: String,
    val userId: String
)