package com.example.theworldofpuppies.booking.vet.data

import com.example.theworldofpuppies.booking.vet.data.dto.VetBookingDto
import com.example.theworldofpuppies.booking.vet.domain.VetBooking
import com.example.theworldofpuppies.core.presentation.util.toLocalDateTime
import com.example.theworldofpuppies.services.vet.data.mappers.toVetTimeSlot

fun VetBookingDto.toVetBooking(): VetBooking {
    return VetBooking(
        id = id,
        publicBookingId = publicBookingId,
        providerId = providerId,
        petId = petId,
        vetBookingSnapshot = vetBookingSnapshot,
        vetTimeSlot = vetTimeSlot.toVetTimeSlot(),
        healthIssues = healthIssues,
        healthIssueDescription = healthIssueDescription,
        address = address,
        basePrice = basePrice,
        discountedPrice = discountedPrice,
        totalPrice = totalPrice,
        discount = discount,
        bookingStatus = bookingStatus,
        cancellationStatus = cancellationStatus,
        paymentStatus = paymentStatus,
        creationDate = creationDate,
        serviceDate = serviceDate?.toLocalDateTime(),
        isRated = isRated
    )
}

