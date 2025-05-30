package com.example.theworldofpuppies.shop.cart.data.mappers

import com.example.theworldofpuppies.shop.cart.data.dto.CartDto
import com.example.theworldofpuppies.shop.cart.domain.Cart

fun CartDto.toCart(): Cart {
    return Cart(
        id = id,
        cartItemIds = cartItemIds,
        cartTotal = totalAmount,
    )
}