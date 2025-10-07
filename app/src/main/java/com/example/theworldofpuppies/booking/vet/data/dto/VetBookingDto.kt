package com.example.theworldofpuppies.booking.vet.data.dto

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.PaymentStatus
import com.example.theworldofpuppies.booking.vet.domain.VetBookingSnapshot
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.vet.data.remote.dto.VetTimeSlotDto
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import kotlinx.serialization.Serializable

@Serializable
data class VetBookingDto(
    val id: String,
    val publicBookingId: String,
    val providerId: String? = null,
    val petId: String,
    val petName: String,
    val breed: String,
    val age: String,
    val vetBookingSnapshot: VetBookingSnapshot,
    val vetTimeSlot: VetTimeSlotDto,
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
    val serviceDate: Long? = null,
    val isRated: Boolean = false
)
