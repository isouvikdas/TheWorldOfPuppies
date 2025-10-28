package com.example.theworldofpuppies.shop.product.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.product.data.local.CategoryEntity

interface CategoryRepository {
    suspend fun fetchAndStoreCategories(): Result<Boolean, NetworkError>
    suspend fun clearAllCategories()
    suspend fun fetchAllCategories(): List<CategoryEntity>
}