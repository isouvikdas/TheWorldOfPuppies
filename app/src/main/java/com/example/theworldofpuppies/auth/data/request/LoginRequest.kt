package com.example.theworldofpuppies.auth.data.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val phoneNumber: String)