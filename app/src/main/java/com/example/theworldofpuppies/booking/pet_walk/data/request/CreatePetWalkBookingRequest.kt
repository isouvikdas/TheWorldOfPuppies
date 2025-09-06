package com.example.theworldofpuppies.booking.pet_walk.data.request

import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import kotlinx.serialization.Serializable

@Serializable
sealed class CreatePetWalkBookingRequest {
    abstract val serviceId: String
    abstract val petId: String
    abstract val notes: String
    abstract val frequency: Frequency

    @Serializable
    data class OneTime(
        override val serviceId: String,
        override val petId: String,
        override val notes: String,
        val serviceDate: Long,
        override val frequency: Frequency
    ) : CreatePetWalkBookingRequest()

    @Serializable
    data class RepeatWeekly(
        override val serviceId: String,
        override val petId: String,
        override val notes: String,
        val startDate: Long,
        val endDate: Long,
        val selectedDays: List<Days>? = null,
        override val frequency: Frequency
    ) : CreatePetWalkBookingRequest()
}