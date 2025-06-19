package com.example.theworldofpuppies.shop.product.presentation.product_list

import com.example.theworldofpuppies.shop.product.domain.Category

data class CategoryListState(
    val categoryList: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCategory: Category? = null,
    val errorMessage: String? = null
)