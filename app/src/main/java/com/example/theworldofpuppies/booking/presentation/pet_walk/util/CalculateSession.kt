package com.example.theworldofpuppies.booking.presentation.pet_walk.util

import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.toDayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun calculateSession(
    selectedDays: List<Days>,
    startDate: LocalDateTime,
    endDate: LocalDateTime
): Int {

    if (selectedDays.isEmpty()) return 0

    val startDateOnly = startDate.toLocalDate()
    val endDateOnly = endDate.toLocalDate()

    val totalDays = ChronoUnit.DAYS.between(startDateOnly, endDateOnly) + 1

    if (totalDays <= 0) return 0

    val selectedDaysOfWeeks = selectedDays.map { it.toDayOfWeek() }.toSet()

    val completeWeeks = (totalDays / 7).toInt()
    val remainingDays = (totalDays % 7).toInt()

    var matchingDaysCount = completeWeeks.times(selectedDaysOfWeeks.size)

    var currentDate = startDateOnly.plusDays(completeWeeks * 7L)
    repeat(remainingDays) {
        if (selectedDaysOfWeeks.contains(currentDate.dayOfWeek)) {
            matchingDaysCount++
        }
        currentDate = currentDate.plusDays(1)
    }

    return matchingDaysCount
}