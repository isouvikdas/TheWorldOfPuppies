package com.example.theworldofpuppies.shop.product.domain

import com.example.theworldofpuppies.shop.product.data.local.CategoryEntity

interface CategoryRepository {
    suspend fun fetchAndStoreCategories(): Boolean
    suspend fun clearAllCategories()
    suspend fun fetchAllCategories(): List<CategoryEntity>
}