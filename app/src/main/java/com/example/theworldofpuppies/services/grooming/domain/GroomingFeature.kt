package com.example.theworldofpuppies.services.grooming.domain

data class GroomingFeature(
    val title: String,
    val features: MutableList<String> = mutableListOf(),
    val price: Double = 0.0,
    val discountedPrice: Double = 0.0
)
