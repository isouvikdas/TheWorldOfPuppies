package com.example.theworldofpuppies.booking.data.grooming.dto

import kotlinx.serialization.Serializable

@Serializable
data class GroomingSlotDto(
    val isAvailable: Boolean,
    val startTime: Long,
    val endTime: Long
)