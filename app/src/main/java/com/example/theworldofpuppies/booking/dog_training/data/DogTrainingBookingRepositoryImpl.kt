package com.example.theworldofpuppies.booking.dog_training.data

import com.example.theworldofpuppies.booking.dog_training.data.mappers.toDogTrainingBooking
import com.example.theworldofpuppies.booking.dog_training.data.remote.DogTrainingBookingApi
import com.example.theworldofpuppies.booking.dog_training.data.remote.request.CreateDogTrainingBookingRequest
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBooking
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBookingRepository
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingFeature
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingOption
import java.time.LocalDateTime

class DogTrainingBookingRepositoryImpl(
    private val api: DogTrainingBookingApi,
    private val userRepository: UserRepository
) : DogTrainingBookingRepository {

    override suspend fun createBooking(
        serviceId: String,
        petId: String,
        notes: String,
        dogTrainingOption: DogTrainingOption?,
        dogTrainingFeatures: List<DogTrainingFeature>,
    ): Result<DogTrainingBooking, NetworkError> {

        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        val request = CreateDogTrainingBookingRequest(
            serviceId = serviceId,
            petId = petId,
            notes = notes,
            dogTrainingOption = dogTrainingOption?.copy(
                dogTrainingFeatures = dogTrainingFeatures
            ) ?: DogTrainingOption()
        )

        return when (val result = api.createDogTrainingBooking(request, token)) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success && response.data != null -> {
                        try {
                            Result.Success(response.data.toDogTrainingBooking())
                        } catch (e: Exception) {
                            Result.Error(NetworkError.SERIALIZATION)
                        }
                    }

                    response.success && response.data == null -> {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }

                    else -> Result.Error(NetworkError.SERVER_ERROR)
                }
            }

            is Result.Error -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}