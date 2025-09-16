package com.example.theworldofpuppies.booking.dog_training.domain

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingOption
import kotlinx.serialization.Serializable

@Serializable
data class DogTrainingSnapshot(
    var name: String? = null,
    var category: Category = Category.DOG_TRAINING,
    var description: String? = null,
    var dogTrainingOption: DogTrainingOption? = null
)
