package com.example.theworldofpuppies.shop.product.presentation.product_list

import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.shop.product.domain.util.ListType

data class ProductListState(
    val productList: List<Product> = emptyList(),
    val listType: ListType = ListType.ALL,
    val isLoading: Boolean = false,
    val isPaginationEnabled: Boolean = false,
    val endOfPaginationReached: Boolean = false,
    val selectedProduct: Product? = null,
    val currentCursor: String? = null,
    val localCursor: Long = 0,
    val errorMessage: String? = null
)