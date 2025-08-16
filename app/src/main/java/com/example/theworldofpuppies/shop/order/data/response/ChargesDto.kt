package com.example.theworldofpuppies.shop.order.data.response

import kotlinx.serialization.Serializable

@Serializable
data class ChargesDto(
    val id: String,
    val shippingFee: Double
)
