package com.example.theworldofpuppies.services.pet_walking.data.remote.dto

import com.example.theworldofpuppies.booking.domain.enums.Category
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import kotlinx.serialization.Serializable

@Serializable
data class PetWalkDto(
    val id: String,
    val name: String,
    val discount: Int,
    val category: Category,
    val frequencies: List<Frequency>,
    val description: String,
    val basePrice: Double,
    val active: Boolean
)

