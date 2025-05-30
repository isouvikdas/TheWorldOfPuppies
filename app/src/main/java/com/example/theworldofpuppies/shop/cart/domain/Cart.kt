package com.example.theworldofpuppies.shop.cart.domain

data class Cart(
    val id: String,
    val cartTotal: Double,
    val cartItemIds: List<String>
)