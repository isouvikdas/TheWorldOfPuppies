package com.example.theworldofpuppies.profile.user.data.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val username: String?,
    val email: String?
)