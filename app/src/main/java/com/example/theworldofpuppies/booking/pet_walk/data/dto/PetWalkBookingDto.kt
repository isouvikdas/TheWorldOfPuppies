package com.example.theworldofpuppies.booking.pet_walk.data.dto

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.booking.grooming.data.dto.GroomingSnapshot
import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.PaymentStatus
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkSnapshot
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import kotlinx.serialization.Serializable

@Serializable
data class PetWalkBookingDto(
    val id: String,
    val publicBookingId: String,
    val providerId: String? = null,
    val petId: String,
    val petWalkSnapshot: PetWalkSnapshot,
    val frequency: Frequency,
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
    val startDate: Long? = null,
    val endDate: Long? = null,
    val selectedDays: List<Days>? = null,
    val isRated: Boolean = false
)