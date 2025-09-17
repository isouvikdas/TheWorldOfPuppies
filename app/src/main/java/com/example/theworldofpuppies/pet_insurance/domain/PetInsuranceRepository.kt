package com.example.theworldofpuppies.pet_insurance.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.pet_insurance.domain.enums.PetType
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed

interface PetInsuranceRepository {
    suspend fun createBooking(
        serviceId: String,
        petType: PetType,
        name: String,
        email: String,
        contactNumber: String,
        age: String,
        breed: String
    ): Result<PetInsuranceBooking, NetworkError>

    suspend fun getPetInsurance(): Result<PetInsurance, NetworkError>

}