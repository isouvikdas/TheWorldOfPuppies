package com.example.theworldofpuppies.pet_insurance.domain

import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.CancellationStatus

data class PetInsuranceBooking(
    val id: String,
    val publicBookingId: String,
    val insuranceDetails: InsuranceDetails,
    val petInsuranceSnapshot: PetInsuranceSnapshot,
    val bookingStatus: BookingStatus,
    val cancellationStatus: CancellationStatus,
    val creationDate: Long
)