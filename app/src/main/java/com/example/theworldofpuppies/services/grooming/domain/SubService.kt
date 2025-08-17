package com.example.theworldofpuppies.services.grooming.domain

import kotlinx.serialization.Serializable

@Serializable
data class SubService(
    val name: String,
    val features: MutableList<String> = mutableListOf(),
    val price: Double = 0.0,
    val discountedPrice: Double = 0.0
)
