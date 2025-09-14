package com.example.theworldofpuppies.booking.vet.data

import com.example.theworldofpuppies.booking.vet.data.request.CreateVetBookingRequest
import com.example.theworldofpuppies.booking.vet.domain.VetBooking
import com.example.theworldofpuppies.booking.vet.domain.VetBookingRepository
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.vet.data.mappers.toVetTimeSlotDto
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot

class VetBookingRepositoryImpl(
    private val api: VetBookingApi,
    private val userRepository: UserRepository
) : VetBookingRepository {

    override suspend fun createVetBooking(
        serviceId: String?,
        vetOptionId: String?,
        petId: String,
        healthIssues: List<HealthIssue>,
        healthIssueDescription: String,
        vetTimeSlot: VetTimeSlot?
    ): Result<VetBooking, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        val request = CreateVetBookingRequest(
            serviceId = serviceId,
            vetOptionId = vetOptionId,
            petId = petId,
            healthIssues = healthIssues,
            healthIssueDescription = healthIssueDescription,
            vetTimeSlot = vetTimeSlot?.toVetTimeSlotDto()
        )

        return when (val result = api.createVetBooking(token, request)) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success && response.data != null -> {
                        try {
                            val vetBooking = response.data.toVetBooking()
                            Result.Success(vetBooking)
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