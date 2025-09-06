package com.example.theworldofpuppies.booking.grooming.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GroomingSlotDto(
    val isAvailable: Boolean,
    val startTime: Long,
    val endTime: Long
)