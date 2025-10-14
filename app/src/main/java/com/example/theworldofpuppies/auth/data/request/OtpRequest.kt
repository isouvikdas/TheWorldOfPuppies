package com.example.theworldofpuppies.auth.data.request

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(val phoneNumber: String, val otp: String, val referralCode: String? = null)
