package com.example.theworldofpuppies.profile.user.data.dto

import com.example.theworldofpuppies.shop.product.data.remote.dto.ImageDto
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    val username: String? = null,
    val email: String? = null,
    val image: ImageDto? = null,
    val fetchUrl: String? = null
)
