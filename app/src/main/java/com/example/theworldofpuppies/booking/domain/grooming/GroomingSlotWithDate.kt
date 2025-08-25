package com.example.theworldofpuppies.booking.domain.grooming

import java.time.LocalDate
import java.time.LocalDateTime

data class GroomingSlotWithDate(
    val date: LocalDateTime,
    val slots: List<GroomingSlot>
)
