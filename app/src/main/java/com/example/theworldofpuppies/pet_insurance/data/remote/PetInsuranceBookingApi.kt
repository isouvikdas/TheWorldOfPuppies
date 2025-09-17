package com.example.theworldofpuppies.pet_insurance.data.remote

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.pet_insurance.data.remote.dto.PetInsuranceBookingDto
import com.example.theworldofpuppies.pet_insurance.data.remote.dto.PetInsuranceDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PetInsuranceBookingApi(private val httpClient: HttpClient) {

    suspend fun createBooking(token: String, request: PetInsuranceBookingRequest)
            : Result<ApiResponse<PetInsuranceBookingDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                constructUrl("booking/insurance/create")
            ) {
                header("Authorization", token)
                setBody(request)
            }
        }
    }

    suspend fun getPetInsurance()
            : Result<ApiResponse<PetInsuranceDto>, NetworkError> {
        return safeCall {
            httpClient.get(constructUrl("booking/service/insurance"))
        }
    }
}