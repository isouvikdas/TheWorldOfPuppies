package com.example.furryroyals.core.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(val message: String, val success: Boolean, val data: T? = null)
