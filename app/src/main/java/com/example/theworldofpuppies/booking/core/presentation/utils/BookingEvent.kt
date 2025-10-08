package com.example.theworldofpuppies.booking.core.presentation.utils

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.core.domain.util.NetworkError

sealed class BookingEvent {
    data class BookingFailed(val error: NetworkError): BookingEvent()
    data class BookingPlaced(val category: Category) : BookingEvent()
}