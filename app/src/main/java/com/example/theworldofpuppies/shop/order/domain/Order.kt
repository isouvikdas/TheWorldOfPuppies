package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.shop.order.domain.enums.OrderStatus
import com.example.theworldofpuppies.shop.order.domain.enums.ReturnStatus

data class Order(
    val id: String,
    val userId: String,
    val orderItemIds: List<String>,
    val totalAmount: Double,
    val cartTotalAmount: Double,
    val orderStatus: OrderStatus,
    val returnStatus: ReturnStatus,
    val orderItems: List<OrderItem> = emptyList(),
    val createdDate: Long,
    val updatedDate: Long
)
