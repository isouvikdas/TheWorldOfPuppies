package com.example.theworldofpuppies.booking.grooming.data

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.booking.grooming.data.dto.GroomingBookingDto
import com.example.theworldofpuppies.booking.grooming.data.dto.GroomingSlotDto
import com.example.theworldofpuppies.booking.grooming.data.request.CreateGroomingBookingRequest
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.services.grooming.domain.SubService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import java.time.LocalDateTime

class GroomingBookingApi(
    private val httpClient: HttpClient
) {

    suspend fun getBookingSlots(forDate: LocalDateTime? = null)
    : Result<ApiResponse<List<GroomingSlotDto>>, NetworkError> {
        return safeCall {
            httpClient.get(
                constructUrl("booking/service/grooming/booking-slot")
            )
        }
    }

    suspend fun bookGrooming(
        serviceId: String,
        subService: SubService,
        selectedSlot: GroomingSlotDto,
        selectedDate: LocalDateTime,
        selectedAddress: Address?= null,
        token: String
    ): Result<ApiResponse<GroomingBookingDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("booking/create")
            ) {
                header("Authorization", token)
                setBody(
                    CreateGroomingBookingRequest(
                        subServiceId = subService.id,
                        serviceId = serviceId,
                        petId = "",
                        notes = "Please be Gentle",
                        serviceDate = selectedDate.toEpochMillis(),
                        groomingTimeSlot = selectedSlot
                    )
                )
            }
        }
    }

}