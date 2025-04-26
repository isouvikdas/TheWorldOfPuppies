package com.example.theworldofpuppies.shop.product.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagedData<T>(
    val products: List<T>,
    val nextCursor: String
)

