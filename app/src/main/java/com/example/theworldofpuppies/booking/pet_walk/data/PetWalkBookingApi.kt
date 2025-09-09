package com.example.theworldofpuppies.booking.pet_walk.data

import com.example.theworldofpuppies.booking.pet_walk.data.dto.PetWalkBookingDto
import com.example.theworldofpuppies.booking.pet_walk.data.request.CreatePetWalkBookingRequest
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PetWalkBookingApi(
    private val httpClient: HttpClient
) {

    suspend fun createPetWalkBooking(token: String, request: CreatePetWalkBookingRequest):
            Result<ApiResponse<PetWalkBookingDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                constructUrl("booking/walking/create")
            ) {
                header("Authorization", token)
                setBody(request)
            }
        }
    }
}