package com.example.theworldofpuppies.shop.product.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val inventory: Int,
    val categoryName: String,
    val imageIds: List<String> = ArrayList(),
    val firstImageId: String? = null,
    var firstImageUri: String? = null,
    val isFeatured: Boolean? = false
)
