package com.example.theworldofpuppies.booking.history.domain

import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBooking
import com.example.theworldofpuppies.booking.grooming.domain.GroomingBooking
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBooking
import com.example.theworldofpuppies.booking.vet.domain.VetBooking
import com.example.theworldofpuppies.core.domain.util.NetworkError

data class BookingHistoryUiState(
    val groomingLoading: Boolean = false,
    val dogTrainingLoading: Boolean = false,
    val vetLoading: Boolean = false,
    val petWalkLoading: Boolean = false,
    val groomingError: NetworkError? = null,
    val vetError: NetworkError? = null,
    val petWalkError: NetworkError? = null,
    val dogTrainingError: NetworkError? = null,
    val petWalkBookings: List<PetWalkBooking> = emptyList(),
    val dogTrainingBookings: List<DogTrainingBooking> = emptyList(),
    val groomingBookings: List<GroomingBooking> = emptyList(),
    val vetBookings: List<VetBooking> = emptyList(),
    val groomingLoaded: Boolean = false,
    val dogTrainingLoaded: Boolean = false,
    val petWalkLoaded: Boolean = false,
    val vetLoaded: Boolean = false
)
