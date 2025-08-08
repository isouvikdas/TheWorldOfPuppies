package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface OrderRepository {
    suspend fun createOrder(): Result<Order, NetworkError>
    suspend fun createCodOrder(): Result<Order, NetworkError>
    suspend fun getOrders(): Result<List<Order>, NetworkError>
    suspend fun getOrderById(orderId: String): Result<Order, NetworkError>
}