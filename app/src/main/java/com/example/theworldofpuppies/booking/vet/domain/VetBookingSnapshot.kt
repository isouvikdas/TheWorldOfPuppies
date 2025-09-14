package com.example.theworldofpuppies.booking.vet.domain

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.services.vet.domain.VetOption
import kotlinx.serialization.Serializable

@Serializable
data class VetBookingSnapshot(
    val name: String,
    val category: Category,
    val vetOption: VetOption,
    val description: String
)
