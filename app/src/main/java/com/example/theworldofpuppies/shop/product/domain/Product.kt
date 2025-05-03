package com.example.theworldofpuppies.shop.product.domain

data class Product(
    val localId: Long,
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val inventory: Int,
    val categoryName: String,
    val imageIds: List<String> = ArrayList(),
    val firstImageId: String? = null,
    val firstImageUri: String? = "",
    val isFeatured: Boolean? = false
)
