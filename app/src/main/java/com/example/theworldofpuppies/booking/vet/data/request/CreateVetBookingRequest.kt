package com.example.theworldofpuppies.booking.vet.data.request

import com.example.theworldofpuppies.services.vet.data.remote.dto.VetTimeSlotDto
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import kotlinx.serialization.Serializable

@Serializable
data class CreateVetBookingRequest(
    val serviceId: String?,
    val vetOptionId: String?,
    val petId: String,
    val healthIssues: List<HealthIssue>,
    val healthIssueDescription: String,
    val vetTimeSlot: VetTimeSlotDto?
)