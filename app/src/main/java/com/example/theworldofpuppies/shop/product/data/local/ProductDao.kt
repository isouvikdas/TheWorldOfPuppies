package com.example.theworldofpuppies.shop.product.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Insert
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM products")
    fun pagingSource(): PagingSource<Int, ProductEntity>

    @Query("DELETE FROM products")
    suspend fun clearAll()

    @Query("SELECT * FROM products WHERE localId = :localId")
    suspend fun getProductByLocalId(localId: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): ProductEntity?

    @Query("SELECT * FROM products WHERE localId > :localCursor ORDER BY localId ASC")
    suspend fun getProductsAfterCursor(localCursor: Long): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id IN (:productIds)")
    suspend fun getProductsByIds(productIds: List<String>): List<ProductEntity>

    @Query("SELECT id FROM products WHERE isFeatured = 1")
    suspend fun getFeaturedProductIds(): List<String>

    @Query("SELECT * FROM products WHERE isFeatured = 1 ORDER BY localId ASC")
    suspend fun getFeaturedProducts(): List<ProductEntity>



}