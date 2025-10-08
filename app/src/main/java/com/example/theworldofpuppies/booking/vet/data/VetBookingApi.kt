package com.example.theworldofpuppies.booking.vet.data

import com.example.theworldofpuppies.booking.vet.data.dto.VetBookingDto
import com.example.theworldofpuppies.booking.vet.data.request.CreateVetBookingRequest
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class VetBookingApi(private val httpClient: HttpClient) {

    suspend fun createVetBooking(token: String, vetBookingRequest: CreateVetBookingRequest)
            : Result<ApiResponse<VetBookingDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                constructUrl("/booking/vet/create")
            ) {
                header("Authorization", token)
                setBody(vetBookingRequest)
            }
        }
    }

}