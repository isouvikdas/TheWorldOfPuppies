package com.example.theworldofpuppies.booking.grooming.domain

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.grooming.domain.GroomingSubService
import java.time.LocalDateTime

interface BookingGroomingRepository {

    suspend fun getGroomingTimeSlots(forDate: LocalDateTime? = null): Result<List<GroomingSlot>, NetworkError>
    suspend fun bookGrooming(
        serviceId: String,
        subService: GroomingSubService,
        selectedSlot: GroomingSlot,
        selectedDate: LocalDateTime,
        selectedAddress: Address
    ): Result<GroomingBooking, NetworkError>
}