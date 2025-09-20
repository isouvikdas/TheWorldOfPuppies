package com.example.theworldofpuppies.profile.pet.domain

import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender

data class Pet(
    val id: String,
    val petImage: PetImage,
    val downloadUrl: String,
    val name: String,
    val gender: Gender,
    val breed: DogBreed,
    val age: String,
    val weight: String,
    val aggression: Aggression,
    val isVaccinated: Boolean,
)
