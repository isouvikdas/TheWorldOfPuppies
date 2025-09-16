package com.example.theworldofpuppies.booking.dog_training.domain

data class DogTrainingBookingUIState(
    val dogTrainingBooking: DogTrainingBooking? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showSuccessDialog: Boolean = false
)
