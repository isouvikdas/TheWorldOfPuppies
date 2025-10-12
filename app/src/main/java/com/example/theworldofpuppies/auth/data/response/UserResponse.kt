package com.example.theworldofpuppies.auth.data.response

import com.example.theworldofpuppies.shop.product.data.remote.dto.ImageDto
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val userId: String, val username: String, val phoneNumber: String,
    val email: String, val token: String, val expirationTime: Long,
    val petIds: List<String>? = null,
    val image: ImageDto? = null,
    val fetchUrl: String? = null
)
