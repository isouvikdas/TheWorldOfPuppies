package com.example.theworldofpuppies.pet_insurance.data.remote.dto

import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.CancellationStatus
import com.example.theworldofpuppies.pet_insurance.domain.InsuranceDetails
import com.example.theworldofpuppies.pet_insurance.domain.PetInsuranceSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class PetInsuranceBookingDto(
    val id: String,
    val publicBookingId: String,
    val insuranceDetails: InsuranceDetails,
    val petInsuranceSnapshot: PetInsuranceSnapshot,
    val bookingStatus: BookingStatus,
    val cancellationStatus: CancellationStatus,
    val creationDate: Long
)
