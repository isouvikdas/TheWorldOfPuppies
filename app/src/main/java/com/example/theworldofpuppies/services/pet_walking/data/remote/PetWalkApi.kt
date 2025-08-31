package com.example.theworldofpuppies.services.pet_walking.data.remote

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.services.pet_walking.data.remote.dto.PetWalkDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class PetWalkApi(
    private val httpClient: HttpClient
) {

    suspend fun getPetWalking(): Result<ApiResponse<PetWalkDto>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("booking/service/walking")
            )
        }
    }
}