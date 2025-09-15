package com.example.theworldofpuppies.services.dog_training.domain

import kotlinx.serialization.Serializable

@Serializable
data class DogTrainingFeature(
    val name: String,
    val description: String,
    val price: Double
)
