package com.example.theworldofpuppies.services.dog_training.data.remote

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.services.dog_training.data.remote.dto.DogTrainingDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class DogTrainingApi(
    private val httpClient: HttpClient
) {

    suspend fun getDogTraining()
            : Result<ApiResponse<DogTrainingDto>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("booking/service/dog-training")
            )
        }
    }
}