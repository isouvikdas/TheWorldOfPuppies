package com.example.theworldofpuppies.services.pet_walking.domain

import java.time.LocalDateTime

data class PetWalkDateRange(
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null
)
