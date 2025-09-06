package com.example.theworldofpuppies.booking.pet_walk.domain

import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBooking
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkDateRange
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import java.time.LocalDateTime

interface PetWalkBookingRepository {
    suspend fun createBooking(
        serviceId: String,
        petId: String,
        notes: String,
        serviceDate: LocalDateTime? = null,
        dateRange: PetWalkDateRange?,
        frequency: Frequency,
        selectedDays: List<Days>? = null
    ): Result<PetWalkBooking, NetworkError>
}