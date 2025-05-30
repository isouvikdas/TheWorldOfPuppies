package com.example.theworldofpuppies.shop.cart.domain

import com.example.theworldofpuppies.shop.product.domain.Product

data class CartItem(
    val id: String,
    var isSelected: Boolean = false,
    var product: Product? = null,
    val productId: String,
    val quantity: Int = 0,
    val localId: Int = 0,
    var price : Double = 0.0,
    var totalPrice: Double = 0.0,
    val cartId: String
)
