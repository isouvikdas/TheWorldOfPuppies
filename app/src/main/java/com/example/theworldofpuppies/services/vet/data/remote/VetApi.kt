package com.example.theworldofpuppies.services.vet.data.remote

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.services.vet.data.remote.dto.VetDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class VetApi(val httpClient: HttpClient) {

    suspend fun getVet()
            : Result<ApiResponse<VetDto>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("booking/service/vet")
            )
        }
    }
}