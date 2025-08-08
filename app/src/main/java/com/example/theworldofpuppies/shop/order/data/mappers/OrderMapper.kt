package com.example.theworldofpuppies.shop.order.data.mappers

import com.example.theworldofpuppies.address.data.mappers.toAddress
import com.example.theworldofpuppies.shop.order.data.response.OrderDto
import com.example.theworldofpuppies.shop.order.data.response.OrderItemDto
import com.example.theworldofpuppies.shop.order.domain.Order
import com.example.theworldofpuppies.shop.order.domain.OrderItem
import java.time.Instant

fun OrderDto.toOrder(): Order {
    return Order(
        id = id,
        publicOrderId = publicOrderId,
        userId = userId,
        orderItemIds = orderItemIds,
        totalAmount = totalAmount,
        cartTotalAmount = cartTotalAmount,
        orderStatus = orderStatus,
        returnStatus = returnStatus,
        createdDate = createdDate,
        updatedDate = updatedDate,
        deliveryDate = deliveryDate,
        address = address.toAddress()
    )
}

fun OrderItemDto.toOrderItem(): OrderItem {
    return OrderItem(
        id = id,
        orderId = orderId,
        publicOrderId = publicOrderId,
        productName = productName,
        productId = productId,
        quantity = quantity,
        returnStatus = returnStatus,
        orderStatus = orderStatus,
        price = price,
        totalPrice = totalPrice
    )
}