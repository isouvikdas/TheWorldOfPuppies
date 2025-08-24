package com.example.theworldofpuppies.booking.data.grooming

import com.example.theworldofpuppies.booking.domain.grooming.BookingRepository
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlot
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toLocalDateTime
import com.example.theworldofpuppies.core.presentation.util.toLocalTime
import java.time.LocalDate

class BookingRepositoryImpl(
    private val api: DummyGroomingBookingApi,
    private val userRepository: UserRepository
) : BookingRepository {

    override suspend fun getGroomingTimeSlots(
        forDate: LocalDate
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
                            GroomingSlot(
                                startTime = dto.startTime.toLocalTime(),
                                endTime = dto.endTime.toLocalTime(),
                                isAvailable = dto.isAvailable
                            )
                        }
                    )

                    // Any kind of failure from backend
                    else -> Result.Error(NetworkError.SERVER_ERROR)
                }
            }
            is Result.Error -> Result.Error(NetworkError.UNKNOWN)
        }
    }

}