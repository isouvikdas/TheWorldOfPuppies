package com.example.theworldofpuppies.pet_insurance.data.remote

import com.example.theworldofpuppies.pet_insurance.domain.InsuranceDetails
import kotlinx.serialization.Serializable

@Serializable
data class PetInsuranceBookingRequest(
    val serviceId: String,
    val insuranceDetails: InsuranceDetails
)
