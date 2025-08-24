package com.example.theworldofpuppies.booking.domain.grooming

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import java.time.LocalDate

interface BookingRepository {

    suspend fun getGroomingTimeSlots(forDate: LocalDate): Result<List<GroomingSlot>, NetworkError>
}