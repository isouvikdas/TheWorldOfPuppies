package com.example.theworldofpuppies.membership.data.dto

import com.example.theworldofpuppies.membership.domain.PremiumOption
import kotlinx.serialization.Serializable

@Serializable
data class PremiumOptionOrderDto(
    val id: String,
    val userId: String,
    val razorpayPaymentId: String = "",
    val isPaymentVerified: Boolean = false,
    val razorpayOrderId: String,
    val keyId: String,
    val amount: Int,
    val premiumOption: PremiumOption,
    val paymentStatus: String,
    val createdDate: Long,
    val endDate: Long
)