package com.example.theworldofpuppies.shop.product.presentation

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.product.domain.Product

data class FeaturedProductListState(
    val isLoading: Boolean = false,
    val productList: List<Product> = emptyList(),
    val errorMessage: String? = null,
    val selectedProduct: Product? = null
)
