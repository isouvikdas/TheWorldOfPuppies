package com.example.theworldofpuppies.booking.grooming.domain

import com.example.theworldofpuppies.booking.grooming.domain.GroomingBooking

data class GroomingBookingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val booking: GroomingBooking? = null,
    val showBookingSuccessDialog: Boolean = false
)