package com.example.theworldofpuppies.shop.cart.data.mappers

import com.example.theworldofpuppies.shop.cart.data.dto.CartItemDto
import com.example.theworldofpuppies.shop.cart.domain.Cart
import com.example.theworldofpuppies.shop.cart.domain.CartItem

fun CartItemDto.toCartItem(): CartItem {
    return CartItem(
        id = id,
        cartId = cartId,
        quantity = quantity,
        productId = productId,
        isSelected = isSelected
    )
}