package com.example.theworldofpuppies.membership.data

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.membership.domain.PremiumOption
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class PremiumOptionApi(
    private val httpClient: HttpClient
) {

    suspend fun getPremiumOptions()
            : Result<ApiResponse<List<PremiumOption>>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("auth/premium-option")
            )
        }
    }
}