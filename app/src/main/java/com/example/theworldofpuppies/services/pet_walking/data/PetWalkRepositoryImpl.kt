package com.example.theworldofpuppies.services.pet_walking.data

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.pet_walking.data.mappers.toPetWalk
import com.example.theworldofpuppies.services.pet_walking.data.remote.PetWalkApi
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalk
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkRepository

class PetWalkRepositoryImpl(
    private val api: PetWalkApi
): PetWalkRepository {

    override suspend fun getPetWalk(): Result<PetWalk, NetworkError> {
        return when (val result = api.getPetWalking()) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success && response.data != null && !response.data.active -> {
                        Result.Error(NetworkError.SERVICE_NOT_AVAILABLE)
                    }
                    response.success && response.data != null -> {
                        Result.Success(response.data.toPetWalk())
                    }

                    response.success || response.data == null -> {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }

                    else -> {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }
                }
            }

            is Result.Error -> {
                Result.Error(NetworkError.UNKNOWN)
            }
        }
    }
}