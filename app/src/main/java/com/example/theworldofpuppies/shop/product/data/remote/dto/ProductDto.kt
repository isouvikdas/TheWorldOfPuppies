package com.example.theworldofpuppies.shop.product.data.remote.dto

import com.example.theworldofpuppies.shop.product.domain.Image
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val discountedPrice: Double,
    val discount: Int,
    val inventory: Int,
    val categoryName: String,
    val images: List<ImageDto> = emptyList(),
    val firstImage: ImageDto? = null,
    val isFeatured: Boolean? = false,
    val isRecommended: Boolean = false,
    val rating: Double = 0.0
)
