package com.example.theworldofpuppies.shop.product.domain

import android.content.Context
import com.example.theworldofpuppies.shop.product.data.local.ProductEntity

interface ProductRepository {
    suspend fun fetchLastFetchedPage(localCursor: Long): List<ProductEntity>
    suspend fun fetchAndStoreProducts(cursor: String?): String?
    suspend fun clearAllProducts()
    suspend fun getProductDetails(productId: String): ProductEntity?
    suspend fun getProductImages(imageId: String): ByteArray?
    suspend fun cacheFirstImage(productEntity: ProductEntity, context: Context): String
}