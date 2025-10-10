package com.example.theworldofpuppies.services.dog_training.data.remote.dto

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingOption
import kotlinx.serialization.Serializable

@Serializable
data class DogTrainingDto(
    val id: String,
    val name: String,
    val description: String,
    val discount: Int,
    val category: Category,
    val dogTrainingOptions: List<DogTrainingOption>,
    val active: Boolean,
    val isRated: Boolean = false,
    val averageStars: Double = 0.0,
    val totalReviews: Int = 0
)
