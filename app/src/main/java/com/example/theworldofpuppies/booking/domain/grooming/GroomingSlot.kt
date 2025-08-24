package com.example.theworldofpuppies.booking.domain.grooming

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class GroomingSlot(
    val isAvailable: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)
