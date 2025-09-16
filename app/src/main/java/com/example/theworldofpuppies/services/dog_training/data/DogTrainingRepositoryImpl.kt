package com.example.theworldofpuppies.services.dog_training.data

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.dog_training.data.mappers.toDogTraining
import com.example.theworldofpuppies.services.dog_training.data.remote.DogTrainingApi
import com.example.theworldofpuppies.services.dog_training.domain.DogTraining
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingRepository

class DogTrainingRepositoryImpl(
    private val api: DogTrainingApi
) : DogTrainingRepository {

    override suspend fun getDogTraining(): Result<DogTraining, NetworkError> {
        return when (val result = api.getDogTraining()) {
            is Result.Success -> {
                val response = result.data

                when {
                    response.success && response.data != null -> {
                        try {
                            Result.Success(response.data.toDogTraining())
                        } catch (e: Exception) {
                            Result.Error(NetworkError.SERIALIZATION)
                        }
                    }

                    response.success && response.data == null ->
                        Result.Error(NetworkError.SERVER_ERROR)

                    else -> Result.Error(NetworkError.UNKNOWN)
                }
            }

            is Result.Error -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}