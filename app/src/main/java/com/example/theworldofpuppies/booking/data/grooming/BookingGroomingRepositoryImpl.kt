package com.example.theworldofpuppies.booking.data.grooming

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.booking.domain.grooming.BookingGroomingRepository
import com.example.theworldofpuppies.booking.domain.grooming.GroomingBooking
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlot
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.grooming.domain.SubService
import java.time.LocalDateTime

class BookingGroomingRepositoryImpl(
    private val api: DummyGroomingBookingApi,
    private val userRepository: UserRepository
) : BookingGroomingRepository {

    override suspend fun getGroomingTimeSlots(
        forDate: LocalDateTime
    ): Result<List<GroomingSlot>, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        return when (val result = api.getBookingSlots(forDate, token)) {
            is Result.Success -> {
                val response = result.data

                when {
                    // Success but no slots
                    response.success && response.data == null -> Result.Success(emptyList())

                    // Success with slots
                    response.success && response.data != null -> Result.Success(
                        response.data.map { dto ->
                            dto.toGroomingSlot()
                        }
                    )

                    // Any kind of failure from backend
                    else -> Result.Error(NetworkError.SERVER_ERROR)
                }
            }

            is Result.Error -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun bookGrooming(
        subService: SubService,
        selectedSlot: GroomingSlot,
        selectedDate: LocalDateTime,
        selectedAddress: Address
    ): Result<GroomingBooking, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return when (val result = api.bookGrooming(
            token = token,
            subService = subService,
            selectedAddress = selectedAddress,
            selectedSlot = selectedSlot.toGroomingSlotDto(),
            selectedDate = selectedDate
        )) {
            is Result.Success -> {
                val response = result.data
                when {
                    !response.success || response.data == null -> Result.Error(NetworkError.SERVER_ERROR)
                    else -> Result.Success(response.data.toGroomingBooking())
                }
            }
            is Result.Error -> Result.Error(NetworkError.UNKNOWN)
        }
    }

}