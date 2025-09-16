package com.example.theworldofpuppies.booking.dog_training.data.remote.request

import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingOption
import kotlinx.serialization.Serializable

@Serializable
data class CreateDogTrainingBookingRequest(
    val serviceId: String,
    val petId: String,
    val notes: String,
    val dogTrainingOption: DogTrainingOption,
)
