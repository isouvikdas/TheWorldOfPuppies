package com.example.theworldofpuppies.auth.data.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val userId: String, val username: String, val phoneNumber: String,
    val email: String, val token: String, val expirationTime: Long
)
