package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.core.domain.util.Error

data class OrderHistoryUiState(
    val orderHistory: MutableList<Order> = mutableListOf(),
    val isLoading: Boolean = false,
    val error: Error? = null
)
