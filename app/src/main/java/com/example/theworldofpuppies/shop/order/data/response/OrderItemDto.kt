package com.example.theworldofpuppies.shop.order.data.response

import com.example.theworldofpuppies.shop.order.domain.enums.OrderStatus
import com.example.theworldofpuppies.shop.order.domain.enums.ReturnStatus
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDto(
    val id: String,
    val orderId: String,
    val publicOrderId: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val returnStatus: ReturnStatus,
    val orderStatus: OrderStatus,
    val price: Double,
    val totalPrice: Double,
    val isRated: Boolean = false
)