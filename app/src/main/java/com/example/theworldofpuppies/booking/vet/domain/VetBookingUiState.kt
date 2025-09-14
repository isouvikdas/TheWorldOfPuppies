package com.example.theworldofpuppies.booking.vet.domain

data class VetBookingUiState(
    val isLoading: Boolean = false,
    val vetBooking: VetBooking? = null,
    val errorMessage: String? = null,
    val showSuccessDialog: Boolean = false
)
