package com.example.theworldofpuppies.services.grooming.domain

import kotlinx.serialization.Serializable

@Serializable
data class GroomingSubService(
    val id: String,
    val name: String,
    val features: List<String> = emptyList(),
    val price: Double = 0.0,
    val discountedPrice: Double = 0.0
)
