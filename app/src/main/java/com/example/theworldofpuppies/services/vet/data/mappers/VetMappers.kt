package com.example.theworldofpuppies.services.vet.data.mappers

import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.core.presentation.util.toLocalDateTime
import com.example.theworldofpuppies.services.vet.data.remote.dto.VetDto
import com.example.theworldofpuppies.services.vet.data.remote.dto.VetTimeSlotDto
import com.example.theworldofpuppies.services.vet.domain.Vet
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot

fun VetDto.toVet(): Vet {
    return Vet(
        id = id,
        name = name,
        discount = discount,
        category = category,
        description = description,
        active = active,
        vetOptions = vetOptions,
        vetTimeSlots = vetTimeSlots.map { it.toVetTimeSlot() },
        healthIssues = healthIssues
    )
}

fun VetTimeSlotDto.toVetTimeSlot(): VetTimeSlot {
    return VetTimeSlot(
        vetBookingCategory = vetBookingCategory,
        dateTime = dateTime.toLocalDateTime(),
        isAvailable = isAvailable
    )
}

fun VetTimeSlot.toVetTimeSlotDto(): VetTimeSlotDto {
    return VetTimeSlotDto(
        vetBookingCategory = vetBookingCategory,
        dateTime = dateTime.toEpochMillis(),
        isAvailable = isAvailable
    )

}