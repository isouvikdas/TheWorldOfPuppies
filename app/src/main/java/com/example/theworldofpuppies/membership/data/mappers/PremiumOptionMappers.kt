package com.example.theworldofpuppies.membership.data.mappers

import com.example.theworldofpuppies.core.presentation.util.toLocalDateTime
import com.example.theworldofpuppies.membership.data.dto.PremiumOptionOrderDto
import com.example.theworldofpuppies.membership.domain.PremiumOptionOrder

fun PremiumOptionOrderDto.toPremiumOptionOrder(): PremiumOptionOrder {
    return PremiumOptionOrder(
        id = id,
        userId = userId,
        razorpayPaymentId = razorpayPaymentId,
        isPaymentVerified = isPaymentVerified,
        razorpayOrderId = razorpayOrderId,
        keyId = keyId,
        amount = amount,
        premiumOption = premiumOption,
        paymentStatus = paymentStatus,
        createdDate = createdDate.toLocalDateTime(),
        endDate = endDate.toLocalDateTime()
    )
}