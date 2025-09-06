package com.example.theworldofpuppies.booking.grooming.domain

import java.time.LocalDateTime

data class GroomingSlot(
    val isAvailable: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)