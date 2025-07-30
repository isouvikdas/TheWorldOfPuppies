package com.example.theworldofpuppies.shop.order.data.response

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    val razorpayOrderId: String,
    val orderId: String,
    val price: Int,
    val keyId: String
)