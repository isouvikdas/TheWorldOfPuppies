package com.example.theworldofpuppies.booking.data.grooming

import com.example.theworldofpuppies.booking.data.grooming.dto.GroomingSlotDto
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlot
import com.example.theworldofpuppies.core.presentation.util.toLocalTime

fun GroomingSlotDto.toGroomingSlot(): GroomingSlot {
    return GroomingSlot(
        isAvailable = this.isAvailable,
        startTime = this.startTime.toLocalTime(),
        endTime = this.endTime.toLocalTime()
    )
}
