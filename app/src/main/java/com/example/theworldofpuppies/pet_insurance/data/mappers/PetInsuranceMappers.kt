package com.example.theworldofpuppies.pet_insurance.data.mappers

import com.example.theworldofpuppies.pet_insurance.data.remote.dto.PetInsuranceBookingDto
import com.example.theworldofpuppies.pet_insurance.data.remote.dto.PetInsuranceDto
import com.example.theworldofpuppies.pet_insurance.domain.PetInsurance
import com.example.theworldofpuppies.pet_insurance.domain.PetInsuranceBooking

fun PetInsuranceBookingDto.toPetInsuranceBooking(): PetInsuranceBooking {
    return PetInsuranceBooking(
        id = id,
        publicBookingId = publicBookingId,
        insuranceDetails = insuranceDetails,
        petInsuranceSnapshot = petInsuranceSnapshot,
        bookingStatus = bookingStatus,
        cancellationStatus = cancellationStatus,
        creationDate = creationDate
    )
}

fun PetInsuranceDto.toPetInsurance(): PetInsurance {
    return PetInsurance(
        id = id,
        name = name,
        category = category,
        description = description,
        active = active
    )
}