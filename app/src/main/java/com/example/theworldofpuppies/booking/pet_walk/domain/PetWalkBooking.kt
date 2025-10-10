package com.example.theworldofpuppies.booking.pet_walk.domain

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.PaymentStatus
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import java.time.LocalDateTime

data class PetWalkBooking(
    val id: String,
    val publicBookingId: String,
    val providerId: String? = null,
    val petId: String,
    val name: String,
    val breed: String,
    val age: String,
    val serviceSnapshot: PetWalkSnapshot,
    val frequency: Frequency,
    val address: AddressDto,
    val basePrice: Double,
    val discountedPrice: Double,
    val totalPrice: Double,
    val discount: Int,
    val bookingStatus: BookingStatus,
    val cancellationStatus: CancellationStatus,
    val paymentStatus: PaymentStatus,
    val creationDate: LocalDateTime,
    val serviceDate: LocalDateTime? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val days: List<Days>? = null,
    val isRated: Boolean = false,
    val averageStars: Double = 0.0,
    val totalReviews: Int = 0
)