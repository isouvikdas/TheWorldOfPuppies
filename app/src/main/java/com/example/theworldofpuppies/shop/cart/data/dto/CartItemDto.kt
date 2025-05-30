package com.example.theworldofpuppies.shop.cart.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartItemDto(
    val id: String,
    val isSelected: Boolean,
    val productId: String,
    val quantity: Int = 0,
    val cartId: String
)