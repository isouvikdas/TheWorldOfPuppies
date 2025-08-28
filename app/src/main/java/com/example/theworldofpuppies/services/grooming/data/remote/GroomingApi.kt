package com.example.theworldofpuppies.services.grooming.data.remote

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.services.grooming.data.remote.dto.GroomingDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GroomingApi(private val httpClient: HttpClient) {

    suspend fun getServiceById(): Result<ApiResponse<GroomingDto>, NetworkError> {
        return safeCall {
            httpClient.get(constructUrl("booking/service/grooming"))
        }
    }
}