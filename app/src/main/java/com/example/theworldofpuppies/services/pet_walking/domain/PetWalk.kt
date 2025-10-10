package com.example.theworldofpuppies.services.pet_walking.domain

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency

data class PetWalk(
    val id: String,
    val name: String,
    val discount: Int,
    val category: Category,
    val frequencies: List<Frequency>,
    val description: String,
    val basePrice: Double,
    val active: Boolean,
    val isRated: Boolean,
    val totalReviews: Int,
    val averageReviews: Double
)
