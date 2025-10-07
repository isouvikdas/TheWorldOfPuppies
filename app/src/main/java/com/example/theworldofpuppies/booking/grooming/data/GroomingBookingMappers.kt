package com.example.theworldofpuppies.booking.grooming.data

import com.example.theworldofpuppies.booking.grooming.data.dto.GroomingBookingDto
import com.example.theworldofpuppies.booking.grooming.data.dto.GroomingSlotDto
import com.example.theworldofpuppies.booking.grooming.domain.GroomingBooking
import com.example.theworldofpuppies.booking.grooming.domain.GroomingSlot
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.core.presentation.util.toLocalDateTime

fun GroomingSlotDto.toGroomingSlot(): GroomingSlot {
    return GroomingSlot(
        isAvailable = this.isAvailable,
        startTime = this.startTime.toLocalDateTime(),
        endTime = this.endTime.toLocalDateTime()
    )
}

fun GroomingSlot.toGroomingSlotDto(): GroomingSlotDto {
    return GroomingSlotDto(
        isAvailable = this.isAvailable,
        startTime = this.startTime.toEpochMillis(),
        endTime = this.endTime.toEpochMillis()
    )
}

fun GroomingBookingDto.toGroomingBooking(): GroomingBooking {
    return GroomingBooking(
        id = this.id,
        publicBookingId = this.publicBookingId,
        providerId = this.providerId ?: "",
        petId = this.petId,
        name = this.petName,
        breed = this.breed,
        age = this.age,
        serviceSnapshot = this.groomingSnapshot,
        address = address,
        basePrice = this.basePrice,
        discountedPrice = this.discountedPrice,
        discount = this.discount,
        bookingStatus = this.bookingStatus,
        cancellationStatus = this.cancellationStatus,
        paymentStatus = this.paymentStatus,
        creationDate = this.creationDate.toLocalDateTime(),
        serviceDate = this.serviceDate.toLocalDateTime(),
        groomingSlot = this.groomingTimeSlot.toGroomingSlot(),
        isRated = isRated,
        totalPrice = totalPrice
    )
}
