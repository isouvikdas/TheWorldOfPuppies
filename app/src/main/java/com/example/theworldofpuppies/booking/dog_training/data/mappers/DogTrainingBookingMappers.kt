package com.example.theworldofpuppies.booking.dog_training.data.mappers

import com.example.theworldofpuppies.address.data.mappers.toAddress
import com.example.theworldofpuppies.booking.dog_training.data.remote.dto.DogTrainingBookingDto
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBooking
import com.example.theworldofpuppies.core.presentation.util.toLocalDateTime

fun DogTrainingBookingDto.toDogTrainingBooking(): DogTrainingBooking {
    return DogTrainingBooking(
        id = id,
        publicBookingId = publicBookingId,
        providerId = providerId ?: "",
        petId = petId,
        dogTrainingSnapshot = dogTrainingSnapshot,
        address = address.toAddress(),
        basePrice = basePrice,
        discountedPrice = discountedPrice,
        totalPrice = totalPrice,
        discount = discount,
        bookingStatus = bookingStatus,
        cancellationStatus = cancellationStatus,
        paymentStatus = paymentStatus,
        creationDate = creationDate.toLocalDateTime(),
        serviceStartDate = serviceStartDate?.toLocalDateTime(),
        serviceEndDate = serviceEndDate?.toLocalDateTime()
    )
}