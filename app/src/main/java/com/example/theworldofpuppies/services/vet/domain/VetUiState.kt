package com.example.theworldofpuppies.services.vet.domain

import com.example.theworldofpuppies.booking.core.domain.Category
import java.time.LocalDateTime

data class VetUiState(
    val selectedDate: LocalDateTime? = null,
    val selectedSlot: VetTimeSlot? = null,
    val timeSlots: List<VetTimeSlot> = emptyList(),
    val slotsPerDate: List<VetTimeSlot> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val vetOptions: List<VetOption> = emptyList(),
    val selectedVetOption: VetOption? = null,
    val name: String? = null,
    val description: String? = null,
    val discount: Int? = 0,
    val category: Category = Category.VETERINARY,
    val active: Boolean = false
)
