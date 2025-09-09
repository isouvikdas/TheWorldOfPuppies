package com.example.theworldofpuppies.booking.pet_walk.data

import com.example.theworldofpuppies.booking.pet_walk.data.dto.PetWalkBookingDto
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBooking
import com.example.theworldofpuppies.core.presentation.util.toLocalDateTime

fun PetWalkBookingDto.toPetWalkBooking(): PetWalkBooking {
    return PetWalkBooking(
        id = id,
        publicBookingId = publicBookingId,
        providerId = providerId,
        petId = petId,
        serviceSnapshot = petWalkSnapshot,
        frequency = frequency,
        address = address,
        basePrice = basePrice,
        discountedPrice = discountedPrice,
        totalPrice = totalPrice,
        discount = discount,
        bookingStatus = bookingStatus,
        cancellationStatus = cancellationStatus,
        paymentStatus = paymentStatus,
        creationDate = creationDate.toLocalDateTime(),
        serviceDate = serviceDate?.toLocalDateTime(),
        startDate = serviceDate?.toLocalDateTime(),
        endDate = serviceDate?.toLocalDateTime(),
        days = selectedDays
    )
}
