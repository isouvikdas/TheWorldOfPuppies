package com.example.theworldofpuppies.auth.presentation.login

import com.example.theworldofpuppies.core.presentation.util.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthEventManager {
    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    suspend fun sendEvent(event: Event) {
        _events.emit(event)
    }
}