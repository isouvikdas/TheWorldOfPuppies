package com.example.theworldofpuppies.pet_insurance.data

import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.pet_insurance.data.mappers.toPetInsurance
import com.example.theworldofpuppies.pet_insurance.data.mappers.toPetInsuranceBooking
import com.example.theworldofpuppies.pet_insurance.data.remote.PetInsuranceBookingApi
import com.example.theworldofpuppies.pet_insurance.data.remote.PetInsuranceBookingRequest
import com.example.theworldofpuppies.pet_insurance.domain.InsuranceDetails
import com.example.theworldofpuppies.pet_insurance.domain.PetInsurance
import com.example.theworldofpuppies.pet_insurance.domain.PetInsuranceBooking
import com.example.theworldofpuppies.pet_insurance.domain.PetInsuranceRepository
import com.example.theworldofpuppies.pet_insurance.domain.enums.PetType
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed

class PetInsuranceRepositoryImpl(
    private val api: PetInsuranceBookingApi,
    private val userRepository: UserRepository
) : PetInsuranceRepository {

    override suspend fun createBooking(
        serviceId: String,
        petType: PetType,
        name: String,
        email: String,
        contactNumber: String,
        age: String,
        breed: String
    ): Result<PetInsuranceBooking, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        val request = PetInsuranceBookingRequest(
            serviceId = serviceId,
            insuranceDetails = InsuranceDetails(
                petType = petType,
                name = name,
                email = email,
                contactNumber = contactNumber,
                age = age,
                breed = breed
            )
        )

        return when (val result = api.createBooking(token, request)) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success && response.data != null -> {
                        try {
                            Result.Success(response.data.toPetInsuranceBooking())
                        } catch (e: Exception) {
                            Result.Error(NetworkError.SERIALIZATION)
                        }
                    }

                    response.success && response.data == null ->
                        Result.Error(NetworkError.SERVER_ERROR)

                    else -> Result.Error(NetworkError.UNKNOWN)
                }
            }

            is Result.Error -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getPetInsurance(): Result<PetInsurance, NetworkError> {
        return when (val result = api.getPetInsurance()) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success && response.data != null -> {
                        try {
                            Result.Success(response.data.toPetInsurance())
                        } catch (e: Exception) {
                            Result.Error(NetworkError.SERIALIZATION)
                        }
                    }

                    response.success && response.data == null ->
                        Result.Error(NetworkError.SERVER_ERROR)

                    else -> Result.Error(NetworkError.UNKNOWN)
                }
            }

            is Result.Error -> Result.Error(NetworkError.UNKNOWN)

        }
    }

}