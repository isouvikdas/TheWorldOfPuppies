package com.example.theworldofpuppies.shop.order.data.mappers

import com.example.theworldofpuppies.shop.order.data.response.OrderDto
import com.example.theworldofpuppies.shop.order.domain.Order

fun OrderDto.toOrder(): Order {
    return Order(
        id = id,
        userId = userId,
        orderItemIds = orderItemIds,
        totalAmount = totalAmount,
        cartTotalAmount = cartTotalAmount,
        orderStatus = orderStatus,
        returnStatus = returnStatus,
        createdDate = createdDate,
        updatedDate = updatedDate
    )
}