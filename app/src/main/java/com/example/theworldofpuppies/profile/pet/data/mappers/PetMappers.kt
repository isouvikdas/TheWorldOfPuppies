package com.example.theworldofpuppies.profile.pet.data.mappers

import com.example.theworldofpuppies.profile.pet.data.remote.dto.PetDto
import com.example.theworldofpuppies.profile.pet.domain.Pet

fun PetDto.toPet(): Pet {
    return Pet(
        id = id,
        petImage = petImage,
        downloadUrl = downloadUrl,
        name = name,
        gender = gender,
        breed = breed,
        age = age,
        weight = weight,
        aggression = aggression,
        isVaccinated = isVaccinated
    )
}