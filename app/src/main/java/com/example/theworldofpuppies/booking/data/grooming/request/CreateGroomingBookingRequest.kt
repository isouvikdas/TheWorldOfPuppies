package com.example.theworldofpuppies.booking.data.grooming.request

import com.example.theworldofpuppies.booking.data.grooming.dto.GroomingSlotDto
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroomingBookingRequest(
    val subServiceId: String,
    val serviceId: String,
    val petId: String,
    val notes: String,
    val serviceDate: Long,
    val groomingTimeSlot: GroomingSlotDto
)

