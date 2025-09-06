package com.example.theworldofpuppies.booking.pet_walk.domain

data class PetWalkBookingUiState(
    val isLoading: Boolean = false,
    val petWalkBooking: PetWalkBooking? = null,
    val errorMessage: String? = null,
    val showSuccessDialog: Boolean = false
)
