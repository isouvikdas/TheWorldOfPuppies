package com.example.theworldofpuppies.services.vet.data

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.vet.data.mappers.toVet
import com.example.theworldofpuppies.services.vet.data.remote.VetApi
import com.example.theworldofpuppies.services.vet.domain.Vet
import com.example.theworldofpuppies.services.vet.domain.VetRepository

class VetRepositoryImpl(
    private val api: VetApi,
) : VetRepository {

    override suspend fun getVet(): Result<Vet, NetworkError> {
        return when (val result = api.getVet()) {
            is Result.Success -> {
                val response = result.data
                when {
                    response.success && response.data != null -> {
                        val vet = response.data.toVet()
                        Result.Success(vet)
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