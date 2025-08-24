package com.example.theworldofpuppies.booking.domain.grooming

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class BookingTimeUiState (
    val selectedDate: LocalDateTime = LocalDateTime.now(),
    val currentTime: LocalDateTime = LocalDateTime.now(),
    val selectedSlot: GroomingSlot? = null,
    val timeSlots: MutableList<GroomingSlotWithDate> = mutableListOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)