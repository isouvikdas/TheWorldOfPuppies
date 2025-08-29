package com.example.theworldofpuppies.booking.domain.grooming

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.booking.data.grooming.dto.ServiceSnapshot
import com.example.theworldofpuppies.booking.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.domain.enums.PaymentStatus
import java.time.LocalDateTime

data class GroomingBooking(
    val id: String,
    val publicBookingId: String,
    val providerId: String,
    val petId: String,
    val serviceSnapshot: ServiceSnapshot,
    val address: AddressDto,
    val basePrice: Double,
    val discountedPrice: Double,
    val discount: Int,
    val bookingStatus: BookingStatus,
    val cancellationStatus: CancellationStatus,
    val paymentStatus: PaymentStatus,
    val creationDate: LocalDateTime,
    val serviceDate: LocalDateTime,
    val groomingSlot: GroomingSlot
)
