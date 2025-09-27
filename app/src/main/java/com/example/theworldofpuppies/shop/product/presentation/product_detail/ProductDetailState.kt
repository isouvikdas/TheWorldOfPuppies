package com.example.theworldofpuppies.shop.product.presentation.product_detail

import com.example.theworldofpuppies.shop.product.domain.Product

data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)