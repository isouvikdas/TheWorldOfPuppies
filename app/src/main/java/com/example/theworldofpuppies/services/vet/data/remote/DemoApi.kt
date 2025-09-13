package com.example.theworldofpuppies.services.vet.data.remote

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.services.vet.data.remote.dto.VetDto
import com.example.theworldofpuppies.services.vet.data.remote.dto.VetTimeSlotDto
import com.example.theworldofpuppies.services.vet.domain.VetOption
import com.example.theworldofpuppies.services.vet.domain.enums.VetBookingCategory
import java.time.LocalDate
import java.time.LocalTime

class DemoApi {
    private fun next14daysTimeSlotsSequence(vetBookingCategory: VetBookingCategory): List<VetTimeSlotDto> {
        val slots = listOf(
            1, 7, 9, 11, 13,
            15, 17, 19, 21
        ).map { time -> LocalTime.of(time, 0) }

        val today = LocalDate.now()

        return (0..13).asSequence()
            .flatMap { dayOffset ->
                val date = today.plusDays(dayOffset.toLong())
                slots.asSequence().mapIndexed { slotIndex, startTime ->
                    VetTimeSlotDto(
                        isAvailable = slotIndex % 2 == 0,
                        dateTime = startTime.atDate(date).toEpochMillis(),
                        vetBookingCategory = vetBookingCategory
                    )
                }
            }
            .toList()
    }

    suspend fun getVet(): Result<ApiResponse<VetDto>, NetworkError> {
        return Result.Success(
            ApiResponse(
                "Fetched successfully",
                true,
                VetDto(
                    id = "sdfrwekjrh34234kljh2",
                    name = "Veterinary",
                    discount = 25,
                    description = "Book a Vet Consultation Near You",
                    vetOptions =
                        listOf(
                            VetOption(
                                id = "In Call Booking",
                                vetBookingCategory = VetBookingCategory.IN_CALL_BOOKING,
                                description = "Quick advice from a trusted vet, anytime, anywhere",
                                price = 199.00
                            ),
                            VetOption(
                                id = "Home Visit",
                                vetBookingCategory = VetBookingCategory.HOME_VISIT,
                                description = "Expert care at your doorstep for stress-free checkups",
                                price = 599.00
                            )
                        ),
                    vetTimeSlots = next14daysTimeSlotsSequence(VetBookingCategory.IN_CALL_BOOKING) + next14daysTimeSlotsSequence(VetBookingCategory.HOME_VISIT),
                    active = true,
                    category = Category.VETERINARY
                )
            )
        )
    }
}