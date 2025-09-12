package com.example.theworldofpuppies.services.vet.data.remote.dto

import com.example.theworldofpuppies.services.vet.domain.enums.VetBookingCategory
import kotlinx.serialization.Serializable

@Serializable
data class VetTimeSlotDto(
    val vetBookingCategory: VetBookingCategory,
    val dateTime: Long,
    val isAvailable: Boolean
)