package com.example.theworldofpuppies.booking.grooming.domain

import com.example.theworldofpuppies.booking.grooming.domain.GroomingSlot
import java.time.LocalDateTime

data class GroomingSlotWithDate(
    val date: LocalDateTime,
    val slots: List<GroomingSlot>
)