package com.example.theworldofpuppies.booking.domain.grooming

import java.time.LocalDate

data class GroomingSlotWithDate(
    val date: LocalDate,
    val slots: List<GroomingSlot>
)
