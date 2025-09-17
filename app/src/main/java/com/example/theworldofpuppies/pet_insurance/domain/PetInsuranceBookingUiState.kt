package com.example.theworldofpuppies.pet_insurance.domain

data class PetInsuranceBookingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val petInsuranceBooking: PetInsuranceBooking? = null,
    val showSuccessDialog: Boolean = false
)
