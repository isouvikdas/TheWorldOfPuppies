package com.example.theworldofpuppies.auth.data.request

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(val phoneNumber: String, val email: String, val username: String)
