package com.example.theworldofpuppies.shop.product.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String,
    val name: String,
    val productIds: List<String> = emptyList()
)
