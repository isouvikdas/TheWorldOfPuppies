package com.example.theworldofpuppies.services.dog_training.domain

import kotlinx.serialization.Serializable

@Serializable
data class DogTrainingOption(
    val name: String,
    val description: String,
    val dogTrainingFeatures: List<DogTrainingFeature>
)