package com.example.theworldofpuppies.membership.domain

import kotlinx.serialization.Serializable

@Serializable
data class PremiumOption(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val discountedPrice: Double,
    val validity: Int
)
