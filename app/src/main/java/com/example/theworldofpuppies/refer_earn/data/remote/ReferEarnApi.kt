package com.example.theworldofpuppies.refer_earn.data.remote

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header

class ReferEarnApi(
    private val httpClient: HttpClient
) {

    suspend fun generateReferralCode(token: String)
            : Result<ApiResponse<String?>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("auth/referrals/generate")
            ) {
                header("Authorization", token)
            }
        }
    }

    suspend fun getWalletBalance(token: String)
            : Result<ApiResponse<Double?>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("auth/referrals/wallet/balance")
            ) {
                header("Authorization", token)
            }
        }
    }
}