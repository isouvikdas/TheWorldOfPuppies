package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.shop.order.domain.enums.OrderStatus
import com.example.theworldofpuppies.shop.order.domain.enums.ReturnStatus

data class OrderItem(
    val id: String,
    val orderId: String,
    val publicOrderId: String,
    val productName: String = "",
    val productId: String,
    val quantity: Int,
    val returnStatus: ReturnStatus,
    val orderStatus: OrderStatus,
    val price: Double,
    val totalPrice: Double
)