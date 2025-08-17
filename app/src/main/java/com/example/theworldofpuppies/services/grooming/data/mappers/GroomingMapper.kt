package com.example.theworldofpuppies.services.grooming.data.mappers

import com.example.theworldofpuppies.services.grooming.data.remote.dto.GroomingDto
import com.example.theworldofpuppies.services.grooming.domain.Grooming

fun GroomingDto.toGrooming(): Grooming {
    return Grooming(
        id = id,
        name = name,
        description = description,
        discount = discount,
        category = category,
        subServiceIds = subServiceIds,
        subServices = petSubServices,
        basePrice = basePrice,
        active = active
    )
}