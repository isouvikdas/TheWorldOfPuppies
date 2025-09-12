package com.example.theworldofpuppies.booking.grooming.domain

data class GroomingBookingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val booking: GroomingBooking? = null,
    val showBookingSuccessDialog: Boolean = false
)