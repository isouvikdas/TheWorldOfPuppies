package com.example.theworldofpuppies.shop.order.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    val price: Double,
    val orderId: String,
    val publicOrderId: String,
    val userId: String
    )