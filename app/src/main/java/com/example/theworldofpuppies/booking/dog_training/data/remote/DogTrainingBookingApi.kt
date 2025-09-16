package com.example.theworldofpuppies.booking.dog_training.data.remote

import com.example.theworldofpuppies.booking.dog_training.data.remote.dto.DogTrainingBookingDto
import com.example.theworldofpuppies.booking.dog_training.data.remote.request.CreateDogTrainingBookingRequest
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class DogTrainingBookingApi(private val httpClient: HttpClient) {

    suspend fun createDogTrainingBooking(request: CreateDogTrainingBookingRequest, token: String)
            : Result<ApiResponse<DogTrainingBookingDto>, NetworkError> {
        return safeCall {
            httpClient.post(constructUrl("booking/dog-training/create")) {
                setBody(request)
                header("Authorization", token)
            }
        }
    }

}