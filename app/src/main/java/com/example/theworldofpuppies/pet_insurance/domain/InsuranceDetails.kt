package com.example.theworldofpuppies.pet_insurance.domain

import com.example.theworldofpuppies.pet_insurance.domain.enums.PetType
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import kotlinx.serialization.Serializable

@Serializable
data class InsuranceDetails(
    val petType: PetType,
    val name: String,
    val email: String,
    val contactNumber: String,
    val age: String,
    val breed: String
)
