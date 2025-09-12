package com.example.theworldofpuppies.services.vet.domain

import com.example.theworldofpuppies.services.vet.domain.enums.VetBookingCategory
import java.time.LocalDateTime

data class VetTimeSlot(
    val vetBookingCategory: VetBookingCategory,
    val dateTime: LocalDateTime,
    val isAvailable: Boolean
)
