package com.example.theworldofpuppies.membership.domain

import java.time.LocalDateTime

data class PremiumOptionOrder(
    val id: String,
    val userId: String,
    val razorpayPaymentId: String = "",
    val isPaymentVerified: Boolean = false,
    val razorpayOrderId: String,
    val keyId: String,
    val amount: Int,
    val premiumOption: PremiumOption,
    val paymentStatus: String,
    val createdDate: LocalDateTime,
    val endDate: LocalDateTime
)