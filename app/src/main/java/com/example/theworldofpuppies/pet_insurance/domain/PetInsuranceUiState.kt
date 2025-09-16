package com.example.theworldofpuppies.pet_insurance.domain

import com.example.theworldofpuppies.pet_insurance.domain.enums.PetType
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed

data class PetInsuranceUiState(
    val petType: PetType = PetType.DOG,
    val name: String? = null,
    val email: String? = null,
    val contactNumber: String? = null,
    val age: String? = null,
    val breed: DogBreed? = null,
    val showModalBottomSheet: Boolean = false,

    val nameError: String? = null,
    val breedError: String? = null,
    val emailError: String? = null,
    val contactNumberError: String? = null,
    val ageError: String? = null,
)
