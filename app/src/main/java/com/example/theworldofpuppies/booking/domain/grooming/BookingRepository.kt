package com.example.theworldofpuppies.booking.domain.grooming

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import java.time.LocalDate
import java.time.LocalDateTime

interface BookingRepository {

    suspend fun getGroomingTimeSlots(forDate: LocalDateTime): Result<List<GroomingSlot>, NetworkError>
}