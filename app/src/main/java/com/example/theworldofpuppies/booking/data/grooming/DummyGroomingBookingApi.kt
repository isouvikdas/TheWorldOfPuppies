package com.example.theworldofpuppies.booking.data.grooming

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.address.data.mappers.toAddressDto
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.booking.data.grooming.dto.GroomingBookingDto
import com.example.theworldofpuppies.booking.data.grooming.dto.GroomingSlotDto
import com.example.theworldofpuppies.booking.data.grooming.dto.ServiceSnapshot
import com.example.theworldofpuppies.booking.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.domain.enums.CancellationStatus
import com.example.theworldofpuppies.booking.domain.enums.PaymentStatus
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.atTimeEpochMillis
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.services.domain.enums.ServiceCategory
import com.example.theworldofpuppies.services.grooming.domain.SubService
import java.time.LocalDateTime
import java.time.LocalTime

class DummyGroomingBookingApi {
    suspend fun getBookingSlots(
        forDate: LocalDateTime,
        token: String
    ): Result<ApiResponse<List<GroomingSlotDto>>, NetworkError> {
        val defaultSlots = listOf(
            LocalTime.of(1, 0) to LocalTime.of(5, 0),
            LocalTime.of(7, 0) to LocalTime.of(9, 0),
            LocalTime.of(9, 0) to LocalTime.of(11, 0),
            LocalTime.of(11, 0) to LocalTime.of(13, 0),
            LocalTime.of(13, 0) to LocalTime.of(15, 0),
            LocalTime.of(15, 0) to LocalTime.of(17, 0),
            LocalTime.of(17, 0) to LocalTime.of(19, 0),
            LocalTime.of(19, 0) to LocalTime.of(21, 0),
            LocalTime.of(21, 0) to LocalTime.of(23, 0)
        )

        var count = 0
        val slots = defaultSlots.map { (start, end) ->
            val startMillis = forDate.atTimeEpochMillis(start)
            val endMillis = forDate.atTimeEpochMillis(end)
            count++
            GroomingSlotDto(
                isAvailable = count % 2 == 0,
                startTime = startMillis,
                endTime = endMillis
            )
        }

        return Result.Success(
            ApiResponse(
                message = "Time slots have been fetched successfully",
                success = true,
                data = slots
            )
        )
    }


    suspend fun bookGrooming(
        token: String,
        subService: SubService,
        selectedAddress: Address,
        selectedSlot: GroomingSlotDto,
        selectedDate: LocalDateTime,
    ): Result<ApiResponse<GroomingBookingDto>, NetworkError> {
        val booking = GroomingBookingDto(
            id = "234245",
            publicBookingId = "BKG-0001",
            providerId = "234245",
            serviceSnapshot = ServiceSnapshot(
                name = "Grooming",
                category = ServiceCategory.GROOMING,
                subService = SubService(
                    id = subService.id,
                    name = subService.name,
                    features = subService.features,
                    price = subService.price
                ),
                description = "",
            ),
            address = selectedAddress.toAddressDto(),
            basePrice = subService.price,
            discountedPrice = subService.discountedPrice,
            discount = 25,
            bookingStatus = BookingStatus.PENDING,
            cancellationStatus = CancellationStatus.NULL,
            paymentStatus = PaymentStatus.AWAITING_PAYMENT,
            creationDate = LocalDateTime.now().atTimeEpochMillis(LocalDateTime.now().toLocalTime()),
            serviceDate = selectedDate.atTimeEpochMillis(selectedDate.toLocalTime()),
            serviceSlot = selectedSlot.startTime
        )
        return Result.Success(
            ApiResponse(
                message = "Booking has been placed successfully, we will let you know once we confirm your booking",
                success = true,
                data = booking
            )
        )
    }

}

