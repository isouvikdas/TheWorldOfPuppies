package com.example.theworldofpuppies.booking.history.data

import com.example.theworldofpuppies.booking.dog_training.data.mappers.toDogTrainingBooking
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBooking
import com.example.theworldofpuppies.booking.grooming.data.toGroomingBooking
import com.example.theworldofpuppies.booking.grooming.domain.GroomingBooking
import com.example.theworldofpuppies.booking.history.domain.BookingHistoryRepository
import com.example.theworldofpuppies.booking.pet_walk.data.toPetWalkBooking
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBooking
import com.example.theworldofpuppies.booking.vet.data.toVetBooking
import com.example.theworldofpuppies.booking.vet.domain.VetBooking
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingHistoryRepositoryImpl(
    private val api: BookingHistoryApi,
    private val userRepository: UserRepository
) : BookingHistoryRepository {

    override suspend fun getGroomingBookings(): Result<List<GroomingBooking>, NetworkError> {
        val userId = userRepository.getUserId()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = api.getGroomingBookings(userId)) {
                is Result.Success -> {
                    val response = result.data
                    when {
                        response.success && response.data.isNullOrEmpty() -> {
                            Result.Error(NetworkError.NO_BOOKING_FOUND)
                        }

                        response.success -> {
                            val bookings = response.data!!.map { it.toGroomingBooking() }
                            Result.Success(bookings)
                        }

                        else -> {
                            Result.Error(NetworkError.SERVER_ERROR)
                        }
                    }
                }

                is Result.Error -> Result.Error(result.error)
            }
        }
    }

    override suspend fun getDogTrainingBookings(): Result<List<DogTrainingBooking>, NetworkError> {
        val userId = userRepository.getUserId()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = api.getDogTrainingBookings(userId)) {
                is Result.Success -> {
                    val response = result.data
                    when {
                        response.success && response.data.isNullOrEmpty() -> {
                            Result.Error(NetworkError.NO_BOOKING_FOUND)
                        }

                        response.success -> {
                            val bookings = response.data!!.map { it.toDogTrainingBooking() }
                            Result.Success(bookings)
                        }

                        else -> Result.Error(NetworkError.SERVER_ERROR)
                    }
                }

                is Result.Error -> Result.Error(result.error)
            }
        }
    }

    override suspend fun getPetWalkBookings(): Result<List<PetWalkBooking>, NetworkError> {
        val userId = userRepository.getUserId()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = api.getPetWalkBookings(userId)) {
                is Result.Success -> {
                    val response = result.data
                    when {
                        response.success && response.data.isNullOrEmpty() -> {
                            Result.Error(NetworkError.NO_BOOKING_FOUND)
                        }

                        response.success -> {
                            val bookings = response.data!!.map { it.toPetWalkBooking() }
                            Result.Success(bookings)
                        }

                        else -> Result.Error(NetworkError.SERVER_ERROR)
                    }
                }

                is Result.Error -> Result.Error(result.error)
            }
        }
    }

    override suspend fun getVetBookings(): Result<List<VetBooking>, NetworkError> {
        val userId = userRepository.getUserId()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = api.getVetBookings(userId)) {
                is Result.Success -> {
                    val response = result.data
                    when {
                        response.success && response.data.isNullOrEmpty() -> {
                            Result.Error(NetworkError.NO_BOOKING_FOUND)
                        }

                        response.success -> {
                            val bookings = response.data!!.map { it.toVetBooking() }
                            Result.Success(bookings)
                        }

                        else -> Result.Error(NetworkError.SERVER_ERROR)
                    }
                }

                is Result.Error -> Result.Error(result.error)
            }
        }
    }
}
