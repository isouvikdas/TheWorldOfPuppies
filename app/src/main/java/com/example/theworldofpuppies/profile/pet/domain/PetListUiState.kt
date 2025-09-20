package com.example.theworldofpuppies.profile.pet.domain

data class PetListUiState(
    val pets: List<Pet> = emptyList(),
    val selectedPet: Pet? = null
)