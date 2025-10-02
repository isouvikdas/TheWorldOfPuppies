package com.example.theworldofpuppies.shop.product.domain

import android.content.Context
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.product.data.local.ProductEntity

interface ProductRepository {
    suspend fun fetchLastFetchedPage(localCursor: Long): List<ProductEntity>
    suspend fun fetchAndStoreProducts(cursor: String?): String?
    suspend fun clearAllProducts()
    suspend fun getProductDetails(productId: String): ProductEntity?
    suspend fun fetchAndStoreFeaturedProducts(): Result<Boolean, NetworkError>
    suspend fun getAllFeaturedProducts(): List<ProductEntity>
}