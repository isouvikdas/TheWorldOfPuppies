package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.shop.order.domain.enums.ReturnStatus

data class OrderItem(
    val id: String,
    val orderId: String,
    val productId: String,
    val quantity: Int,
    val returnStatus: ReturnStatus,
    val price: Double
)