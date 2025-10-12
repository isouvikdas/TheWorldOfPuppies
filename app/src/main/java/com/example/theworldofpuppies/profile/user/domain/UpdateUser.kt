package com.example.theworldofpuppies.profile.user.domain

import com.example.theworldofpuppies.shop.product.domain.Image

data class UpdateUser(
    val email: String?,
    val username: String?,
    val image: Image?,
    val fetchUrl: String?
)
