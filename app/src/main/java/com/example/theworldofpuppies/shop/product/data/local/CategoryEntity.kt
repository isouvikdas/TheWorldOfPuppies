package com.example.theworldofpuppies.shop.product.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.theworldofpuppies.shop.product.domain.Image

@Entity("Categories")
data class CategoryEntity(
    val id: String,
    val name: String,
    val productIds: List<String> = emptyList(),
    val image : Image? = null,
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0
)
