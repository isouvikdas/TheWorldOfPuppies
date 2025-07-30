package com.example.theworldofpuppies.shop.order.data.response

import com.example.theworldofpuppies.shop.order.domain.enums.OrderStatus
import com.example.theworldofpuppies.shop.order.domain.enums.ReturnStatus
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: String,
    val userId: String,
    val orderItemIds: List<String>,
    val totalAmount: Double,
    val cartTotalAmount: Double,
    val orderStatus: OrderStatus,
    val returnStatus: ReturnStatus,
    val createdDate: Long,
    val updatedDate: Long
)
