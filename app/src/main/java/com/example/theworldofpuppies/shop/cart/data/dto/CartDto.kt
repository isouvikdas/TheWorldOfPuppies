package com.example.theworldofpuppies.shop.cart.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartDto(
    val id: String,
    val totalAmount: Double = 0.0,
    val cartItemIds: List<String> = emptyList(),
)