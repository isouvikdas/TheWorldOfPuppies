package com.example.theworldofpuppies.profile.pet.domain

import android.net.Uri
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender

interface PetRepository {
    suspend fun addPet(
        imageUri: Uri,
        name: String,
        breed: DogBreed,
        age: String,
        gender: Gender,
        aggression: Aggression,
        isVaccinated: Boolean,
        weight: String
    ): Result<Pet, NetworkError>

    suspend fun updatePet(
        imageUri: Uri ? = null,
        id: String,
        name: String,
        breed: DogBreed,
        age: String,
        gender: Gender,
        aggression: Aggression,
        isVaccinated: Boolean,
        weight: String
    ): Result<Pet, NetworkError>

    suspend fun getPets(petIds: List<String>): Result<List<Pet>, NetworkError>

    suspend fun deletePet(petId: String): Result<Boolean, NetworkError>
}