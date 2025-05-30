package com.example.theworldofpuppies.shop.cart.presentation

import com.example.theworldofpuppies.shop.cart.domain.Cart
import com.example.theworldofpuppies.shop.cart.domain.CartItem

data class CartUiState(
    val cart: Cart? = null,
    val cartItems: List<CartItem>? = emptyList(),
    var cartTotal: Double = 0.0,
    var totalSelectedItems: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
