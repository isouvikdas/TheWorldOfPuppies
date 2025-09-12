package com.example.theworldofpuppies.services.vet.data.remote.dto

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.services.vet.domain.VetOption
import kotlinx.serialization.Serializable

@Serializable
data class VetDto(
    val id: String,
    val name: String,
    val discount: Int,
    val description: String,
    val category: Category,
    val vetOptions: List<VetOption>,
    val vetTimeSlots: List<VetTimeSlotDto>,
    val active: Boolean
)
