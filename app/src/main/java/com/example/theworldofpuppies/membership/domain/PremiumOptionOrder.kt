package com.example.theworldofpuppies.membership.domain

import kotlinx.serialization.Serializable

@Serializable
data class PremiumOptionOrder(
    val id: String? = null,
    val userId: String,
    val razorpayPaymentId: String = "",
    val isPaymentVerified: Boolean = false,
    val razorpayOrderId: String,
    val keyId: String,
    val amount: Double,
    val premiumOption: PremiumOption,
    val paymentStatus: String,
    val createdDate: Long,
    val endDate: Long
)