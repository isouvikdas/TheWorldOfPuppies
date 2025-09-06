package com.example.theworldofpuppies.booking.pet_walk.data

import android.util.Log
import com.example.theworldofpuppies.booking.pet_walk.data.PetWalkBookingApi
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBooking
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBookingRepository
import com.example.theworldofpuppies.booking.pet_walk.data.request.CreatePetWalkBookingRequest
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkDateRange
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import java.time.LocalDateTime

class PetWalkBookingRepositoryImpl(
    private val api: PetWalkBookingApi,
    private val userRepository: UserRepository
): PetWalkBookingRepository {
    override suspend fun createBooking(
        serviceId: String,
        petId: String,
        notes: String,
        serviceDate: LocalDateTime?,
        dateRange: PetWalkDateRange?,
        frequency: Frequency,
        selectedDays: List<Days>?
    ): Result<PetWalkBooking, NetworkError> {

        try {
            val token = userRepository.getToken()
            if (token == null) {
                return Result.Error(NetworkError.UNAUTHORIZED)
            }
            // Validation with detailed logging
            when (frequency) {
                Frequency.ONE_TIME -> {
                    if (serviceDate == null) {
                        return Result.Error(NetworkError.INVALID_REQUEST)
                    }
                }

                Frequency.REPEAT_WEEKLY -> {
                    if (dateRange == null) {
                        return Result.Error(NetworkError.INVALID_REQUEST)
                    }
                    if (dateRange.startDate == null) {
                        return Result.Error(NetworkError.INVALID_REQUEST)
                    }
                    if (dateRange.endDate == null) {
                        return Result.Error(NetworkError.INVALID_REQUEST)
                    }
                }
            }

            val request = try {
                when (frequency) {
                    Frequency.ONE_TIME -> {
                        val epochMillis = serviceDate!!.toEpochMillis()

                        CreatePetWalkBookingRequest.OneTime(
                            serviceId = serviceId,
                            petId = petId,
                            notes = notes,
                            serviceDate = epochMillis,
                            frequency = frequency
                        )
                    }

                    Frequency.REPEAT_WEEKLY -> {
                        val startEpoch = dateRange!!.startDate!!.toEpochMillis()
                        val endEpoch = dateRange.endDate!!.toEpochMillis()

                        CreatePetWalkBookingRequest.RepeatWeekly(
                            serviceId = serviceId,
                            petId = petId,
                            notes = notes,
                            startDate = startEpoch,
                            endDate = endEpoch,
                            selectedDays = selectedDays,
                            frequency = frequency
                        )
                    }
                }
            } catch (e: Exception) {
                return Result.Error(NetworkError.INVALID_REQUEST)
            }

            return when (val result = api.createPetWalkBooking(token, request)) {
                is Result.Success -> {
                    val response = result.data

                    when {
                        response.success && response.data != null -> {
                            try {
                                val petWalkBooking = response.data.toPetWalkBooking()
                                Result.Success(petWalkBooking)
                            } catch (e: Exception) {
                                Result.Error(NetworkError.SERIALIZATION)
                            }
                        }

                        response.success && response.data == null -> {
                            Result.Error(NetworkError.SERVER_ERROR)
                        }

                        else -> {
                            Result.Error(NetworkError.SERVER_ERROR)
                        }
                    }
                }

                is Result.Error -> {
                    Result.Error(NetworkError.UNKNOWN)
                }
            }

        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }
    }}