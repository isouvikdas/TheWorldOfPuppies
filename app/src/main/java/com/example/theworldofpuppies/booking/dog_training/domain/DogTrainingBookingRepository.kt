package com.example.theworldofpuppies.booking.dog_training.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingFeature
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingOption
import java.time.LocalDateTime

interface DogTrainingBookingRepository {
    suspend fun createBooking(
        serviceId: String,
        petId: String,
        notes: String,
        dogTrainingOption: DogTrainingOption?,
        dogTrainingFeatures: List<DogTrainingFeature>,
    ): Result<DogTrainingBooking, NetworkError>
}