package com.example.theworldofpuppies.shop.product.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Categories")
data class CategoryEntity(
    val id: String,
    val name: String,
    val productIds: List<String> = emptyList(),
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0
)
