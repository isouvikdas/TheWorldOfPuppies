package com.example.theworldofpuppies.booking.domain.grooming

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.grooming.domain.Grooming
import com.example.theworldofpuppies.services.grooming.domain.SubService
import java.time.LocalDateTime

interface BookingGroomingRepository {

    suspend fun getGroomingTimeSlots(forDate: LocalDateTime? = null): Result<List<GroomingSlot>, NetworkError>
    suspend fun bookGrooming(
        serviceId: String,
        subService: SubService,
        selectedSlot: GroomingSlot,
        selectedDate: LocalDateTime,
        selectedAddress: Address
    ): Result<GroomingBooking, NetworkError>
}