package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface OrderRepository {
    suspend fun createOrder(): Result<Order, NetworkError>
    suspend fun createCodOrder(): Result<Order, NetworkError>
}