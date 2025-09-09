package com.example.theworldofpuppies.booking.pet_walk.domain

import com.example.theworldofpuppies.booking.core.domain.Category
import kotlinx.serialization.Serializable

@Serializable
data class PetWalkSnapshot(
    val name: String,
    val category: Category,
    val description: String
)
