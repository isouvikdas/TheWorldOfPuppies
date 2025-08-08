package com.example.theworldofpuppies.shop.order.data.response

import kotlinx.serialization.Serializable

@Serializable
data class OrderFetchResponse(
    val order: OrderDto,
    val orderItems: List<OrderItemDto>
)
