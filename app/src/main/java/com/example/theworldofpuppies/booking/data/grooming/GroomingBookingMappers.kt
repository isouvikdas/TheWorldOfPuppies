package com.example.theworldofpuppies.booking.data.grooming

import com.example.theworldofpuppies.booking.data.grooming.dto.GroomingBookingDto
import com.example.theworldofpuppies.booking.data.grooming.dto.GroomingSlotDto
import com.example.theworldofpuppies.booking.domain.grooming.GroomingBooking
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlot
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
        serviceSnapshot = this.serviceSnapshot,
        address = address,
        basePrice = this.basePrice,
        discountedPrice = this.discountedPrice,
        discount = this.discount,
        bookingStatus = this.bookingStatus,
        cancellationStatus = this.cancellationStatus,
        paymentStatus = this.paymentStatus,
        creationDate = this.creationDate.toLocalDateTime(),
        serviceDate = this.serviceDate.toLocalDateTime(),
        groomingSlot = this.groomingTimeSlot.toGroomingSlot()
    )
}
