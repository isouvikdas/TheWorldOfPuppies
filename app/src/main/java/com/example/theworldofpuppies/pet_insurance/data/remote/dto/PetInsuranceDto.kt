package com.example.theworldofpuppies.pet_insurance.data.remote.dto

import com.example.theworldofpuppies.booking.core.domain.Category
import kotlinx.serialization.Serializable

@Serializable
data class PetInsuranceDto(
    val id: String,
    val name: String,
    val category: Category,
    val description: String,
    val active: Boolean
)
