package com.example.theworldofpuppies.services.pet_walking.data.mappers

import com.example.theworldofpuppies.services.pet_walking.data.remote.dto.PetWalkDto
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalk

fun PetWalkDto.toPetWalk(): PetWalk {
    return PetWalk(
        id = id,
        name = name,
        discount = discount,
        category = category,
        frequencies = frequencies,
        description = description,
        basePrice = basePrice,
        active = active,
        isRated = isRated,
        totalReviews = totalReviews,
        averageReviews = averageReviews

    )
}