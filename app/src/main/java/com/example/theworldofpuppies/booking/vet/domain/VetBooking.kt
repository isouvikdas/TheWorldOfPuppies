package com.example.theworldofpuppies.booking.vet.domain

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.PaymentStatus
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot
import java.time.LocalDateTime

data class VetBooking(
    val id: String,
    val publicBookingId: String,
    val providerId: String? = null,
    val petId: String,
    val vetBookingSnapshot: VetBookingSnapshot,
    val vetTimeSlot: VetTimeSlot,
    val healthIssues: List<HealthIssue>,
    val healthIssueDescription: String,
    val address: AddressDto,
    val basePrice: Double,
    val discountedPrice: Double,
    val totalPrice: Double,
    val discount: Int,
    val bookingStatus: BookingStatus,
    val cancellationStatus: CancellationStatus,
    val paymentStatus: PaymentStatus,
    val creationDate: Long,
    val serviceDate: LocalDateTime? = null,
    val isRated: Boolean
)
