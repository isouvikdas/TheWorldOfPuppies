package com.example.theworldofpuppies.services.dog_training.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface DogTrainingRepository {
    suspend fun getDogTraining(): Result<DogTraining, NetworkError>
}