package com.example.theworldofpuppies.booking.core.presentation.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class BookingEventManager {
    private val _events = MutableSharedFlow<BookingEvent>()
    val events: SharedFlow<BookingEvent> = _events.asSharedFlow()

    suspend fun sendEvent(event: BookingEvent) {
        _events.emit(event)
    }
}