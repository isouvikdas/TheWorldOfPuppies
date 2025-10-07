package com.example.theworldofpuppies.booking.history.domain

import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBooking
import com.example.theworldofpuppies.booking.grooming.domain.GroomingBooking
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBooking
import com.example.theworldofpuppies.booking.vet.domain.VetBooking
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface BookingHistoryRepository {
    suspend fun getGroomingBookings(): Result<List<GroomingBooking>, NetworkError>
    suspend fun getDogTrainingBookings(): Result<List<DogTrainingBooking>, NetworkError>
    suspend fun getPetWalkBookings(): Result<List<PetWalkBooking>, NetworkError>
    suspend fun getVetBookings(): Result<List<VetBooking>, NetworkError>
}