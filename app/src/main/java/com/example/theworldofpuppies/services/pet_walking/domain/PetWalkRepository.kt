package com.example.theworldofpuppies.services.pet_walking.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface PetWalkRepository {

    suspend fun getPetWalk(): Result<PetWalk, NetworkError>
}