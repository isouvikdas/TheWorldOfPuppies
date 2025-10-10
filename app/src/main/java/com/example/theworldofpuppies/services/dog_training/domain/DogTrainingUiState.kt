package com.example.theworldofpuppies.services.dog_training.domain

data class DogTrainingUiState(
    val dogTraining: DogTraining? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val dogTrainingOptions: List<DogTrainingOption> = emptyList(),
    val selectedDogTrainingOption: DogTrainingOption? = null,
    val selectedDogTrainingFeatures: List<DogTrainingFeature> = emptyList(),
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val discount: Int? = 0,
    val isRated: Boolean = false,
    val totalReviews: Int = 0,
    val averageReviews: Double = 0.0
)