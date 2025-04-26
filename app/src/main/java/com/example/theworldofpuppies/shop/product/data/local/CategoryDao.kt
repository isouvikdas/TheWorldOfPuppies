package com.example.theworldofpuppies.shop.product.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertAll(products: List<CategoryEntity>)

    @Query("SELECT * FROM categories")
    fun getCategories(): List<CategoryEntity>

    @Query("DELETE FROM categories")
    suspend fun clearAll()

    @Query("SELECT * FROM categories WHERE localId = :localId")
    suspend fun getProductByLocalId(localId: Long): CategoryEntity?

}