package com.example.theworldofpuppies.booking.data.grooming.dto

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.booking.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.domain.enums.PaymentStatus
import kotlinx.serialization.Serializable

@Serializable
data class GroomingBookingDto(
    val id: String,
    val publicBookingId: String,
    val providerId: String? = null,
    val petId: String,
    val serviceSnapshot: ServiceSnapshot,
    val address: AddressDto,
    val basePrice: Double,
    val discountedPrice: Double,
    val discount: Int,
    val bookingStatus: BookingStatus,
    val cancellationStatus: CancellationStatus,
    val paymentStatus: PaymentStatus,
    val creationDate: Long,
    val serviceDate: Long,
    val groomingTimeSlot: GroomingSlotDto
)