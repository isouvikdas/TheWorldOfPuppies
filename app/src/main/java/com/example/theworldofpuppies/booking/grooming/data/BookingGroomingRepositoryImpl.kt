package com.example.theworldofpuppies.booking.grooming.data

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.booking.grooming.domain.BookingGroomingRepository
import com.example.theworldofpuppies.booking.grooming.domain.GroomingBooking
import com.example.theworldofpuppies.booking.grooming.domain.GroomingSlot
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.grooming.domain.SubService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class BookingGroomingRepositoryImpl(
    private val api: GroomingBookingApi,
    private val userRepository: UserRepository
) : BookingGroomingRepository {

    override suspend fun getGroomingTimeSlots(
        forDate: LocalDateTime?
    ): Result<List<GroomingSlot>, NetworkError> {

        return withContext(Dispatchers.IO) {
            when (val result = api.getBookingSlots(forDate)) {
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
    }

    override suspend fun bookGrooming(
        serviceId: String,
        subService: SubService,
        selectedSlot: GroomingSlot,
        selectedDate: LocalDateTime,
        selectedAddress: Address
    ): Result<GroomingBooking, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = api.bookGrooming(
                token = token,
                subService = subService,
                selectedAddress = selectedAddress,
                selectedSlot = selectedSlot.toGroomingSlotDto(),
                selectedDate = selectedDate,
                serviceId = serviceId
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

}