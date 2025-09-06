package com.example.theworldofpuppies.booking.grooming.domain

import com.example.theworldofpuppies.booking.grooming.domain.GroomingSlot
import java.time.LocalDateTime

data class GroomingTimeUiState (
    val selectedDate: LocalDateTime = LocalDateTime.now(),
    val currentTime: LocalDateTime = LocalDateTime.now(),
    val selectedSlot: GroomingSlot? = null,
    val timeSlots: List<GroomingSlotWithDate> = emptyList(),
    val slotPerDate: List<GroomingSlot> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)