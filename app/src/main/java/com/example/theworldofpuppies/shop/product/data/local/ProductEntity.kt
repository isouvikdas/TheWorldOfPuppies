package com.example.theworldofpuppies.shop.product.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.theworldofpuppies.shop.product.domain.Image

@Entity("Products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val discountedPrice: Double,
    val discount: Int,
    val inventory: Int,
    val categoryName: String,
    val images: List<Image> = emptyList(),
    val firstImage: Image? = null,
    val isFeatured: Boolean? = false,
    val isRecommended: Boolean = false,
    val rating: Double = 0.0
)
