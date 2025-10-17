package com.example.theworldofpuppies.membership.domain

data class PremiumOption(
    val name: String,
    val description: String,
    val price: Double,
    val discountedPrice: Double,
    val validity: Int
)
