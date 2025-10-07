package com.example.theworldofpuppies.booking.history.data

import com.example.theworldofpuppies.booking.dog_training.data.remote.dto.DogTrainingBookingDto
import com.example.theworldofpuppies.booking.grooming.data.dto.GroomingBookingDto
import com.example.theworldofpuppies.booking.pet_walk.data.dto.PetWalkBookingDto
import com.example.theworldofpuppies.booking.vet.data.dto.VetBookingDto
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BookingHistoryApi(
    private val httpClient: HttpClient
) {
    suspend fun getPetWalkBookings(userId: String)
            : Result<ApiResponse<List<PetWalkBookingDto>>, NetworkError> {
        return safeCall {
            httpClient.get(constructUrl("booking/walking/user")) {
                parameter("userId", userId)
            }
        }
    }

    suspend fun getDogTrainingBookings(userId: String)
            : Result<ApiResponse<List<DogTrainingBookingDto>>, NetworkError> {
        return safeCall {
            httpClient.get(constructUrl("booking/dog-training/user")) {
                parameter("userId", userId)
            }
        }
    }


    suspend fun getGroomingBookings(userId: String)
            : Result<ApiResponse<List<GroomingBookingDto>>, NetworkError> {
        return safeCall {
            httpClient.get(constructUrl("booking/grooming/user")) {
                parameter("userId", userId)
            }
        }
    }

    suspend fun getVetBookings(userId: String)
            : Result<ApiResponse<List<VetBookingDto>>, NetworkError> {
        return safeCall {
            httpClient.get(constructUrl("booking/vet/user")) {
                parameter("userId", userId)
            }
        }
    }
}