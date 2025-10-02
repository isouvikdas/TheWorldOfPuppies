package com.example.theworldofpuppies.review.presentation.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ReviewEventManager {
    private val _events = MutableSharedFlow<ReviewEvent>()
    val events: SharedFlow<ReviewEvent> = _events.asSharedFlow()

    suspend fun sendEvent(event: ReviewEvent) {
        _events.emit(event)
    }

}