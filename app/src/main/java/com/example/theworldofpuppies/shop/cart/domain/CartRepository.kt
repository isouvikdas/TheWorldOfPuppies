package com.example.theworldofpuppies.shop.cart.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface CartRepository {
    suspend fun getUserCart(): Result<Cart, NetworkError>
    suspend fun getCartItems(): Result<List<CartItem>, NetworkError>
    suspend fun updateItemSelection(cartItemId: String, isSelected: Boolean): Result<Boolean, NetworkError>
    suspend fun addToCart(productId: String, quantity: Int, isNewItem: Boolean): Result<CartItem, NetworkError>
    suspend fun removeCartItem(cartItemId: String): Result<Boolean, NetworkError>
}