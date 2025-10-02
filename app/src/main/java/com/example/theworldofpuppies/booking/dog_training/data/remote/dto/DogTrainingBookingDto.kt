package com.example.theworldofpuppies.booking.dog_training.data.remote.dto

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingSnapshot
import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.PaymentStatus
import kotlinx.serialization.Serializable

@Serializable
data class DogTrainingBookingDto(
    var id: String,
    var publicBookingId: String,
    var providerId: String? = null,
    var petId: String,
    var dogTrainingSnapshot: DogTrainingSnapshot,
    var address: AddressDto,
    var basePrice: Double,
    var discountedPrice: Double,
    var totalPrice: Double,
    var discount: Int,
    var bookingStatus: BookingStatus,
    var cancellationStatus: CancellationStatus,
    var paymentStatus: PaymentStatus,
    var creationDate: Long,
    var serviceStartDate: Long? = null,
    var serviceEndDate: Long? = null,
    val isRated: Boolean = false
)
