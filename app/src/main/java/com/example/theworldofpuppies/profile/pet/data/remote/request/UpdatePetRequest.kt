package com.example.theworldofpuppies.profile.pet.data.remote.request

import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePetRequest(
    val id: String,
    val name: String,
    val gender: Gender,
    val breed: DogBreed,
    val age: String,
    val weight: String,
    val aggression: Aggression,
    val isVaccinated: Boolean
)
