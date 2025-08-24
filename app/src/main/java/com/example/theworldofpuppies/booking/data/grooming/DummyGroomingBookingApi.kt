package com.example.theworldofpuppies.booking.data.grooming

import com.example.theworldofpuppies.booking.data.grooming.dto.GroomingSlotDto
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class DummyGroomingBookingApi {
    fun getBookingSlots(forDate: LocalDate, token: String): Result<ApiResponse<List<GroomingSlotDto>>, NetworkError> {
        val defaultSlots = listOf(
            LocalTime.of(9, 0) to LocalTime.of(11, 0),
            LocalTime.of(11, 0) to LocalTime.of(13, 0),
            LocalTime.of(13, 0) to LocalTime.of(15, 0),
            LocalTime.of(15, 0) to LocalTime.of(17, 0),
            LocalTime.of(17, 0) to LocalTime.of(19, 0),
            LocalTime.of(19, 0) to LocalTime.of(21, 0),
            LocalTime.of(21, 0) to LocalTime.of(23, 0)
        )
        var count = 0

        return Result.Success(
            ApiResponse(
                message = "time slots have been fetched successfully",
                success = true,
                data = defaultSlots.map { (start, end) ->
                    val startMillis = forDate.atTimeEpochMillis(start)
                    val endMillis = forDate.atTimeEpochMillis(end)
                    count ++
                    GroomingSlotDto(if (count % 2 == 0) true else false,startMillis, endMillis)
                })
        )
    }
}

fun LocalDateTime.toEpochMillis(zone: ZoneId = ZoneId.systemDefault()): Long {
    return this.atZone(zone).toInstant().toEpochMilli()
}

fun LocalDate.atTimeEpochMillis(time: LocalTime, zone: ZoneId = ZoneId.systemDefault()): Long {
    return this.atTime(time).toEpochMillis(zone)
}
