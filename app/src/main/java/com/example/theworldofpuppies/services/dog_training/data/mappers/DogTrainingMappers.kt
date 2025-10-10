package com.example.theworldofpuppies.services.dog_training.data.mappers

import com.example.theworldofpuppies.services.dog_training.data.remote.dto.DogTrainingDto
import com.example.theworldofpuppies.services.dog_training.domain.DogTraining

fun DogTrainingDto.toDogTraining(): DogTraining {
    return DogTraining(
        id = id,
        name = name,
        description = description,
        discount = discount,
        category = category,
        dogTrainingOptions = dogTrainingOptions,
        active = active,
        isRated = isRated,
        averageStars = averageStars,
        totalReviews = totalReviews
    )
}