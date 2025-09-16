package com.example.theworldofpuppies.booking.dog_training.domain

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.PaymentStatus
import java.time.LocalDateTime

data class DogTrainingBooking(
    var id: String,
    var publicBookingId: String,
    var providerId: String,
    var petId: String,
    var dogTrainingSnapshot: DogTrainingSnapshot,
    var address: Address,
    var basePrice: Double,
    var discountedPrice: Double,
    var totalPrice: Double,
    var discount: Int,
    var bookingStatus: BookingStatus,
    var cancellationStatus: CancellationStatus,
    var paymentStatus: PaymentStatus,
    var creationDate: LocalDateTime? = null,
    var serviceStartDate: LocalDateTime? = null,
    var serviceEndDate: LocalDateTime? = null
)
