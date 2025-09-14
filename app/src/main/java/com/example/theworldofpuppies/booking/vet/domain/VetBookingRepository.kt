package com.example.theworldofpuppies.booking.vet.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot

interface VetBookingRepository {
    suspend fun createVetBooking(
        serviceId: String?,
        vetOptionId: String?,
        petId: String,
        healthIssues: List<HealthIssue>,
        healthIssueDescription: String,
        vetTimeSlot: VetTimeSlot?
    ): Result<VetBooking, NetworkError>
}