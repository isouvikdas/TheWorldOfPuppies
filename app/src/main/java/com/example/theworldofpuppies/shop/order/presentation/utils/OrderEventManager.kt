package com.example.theworldofpuppies.shop.order.presentation.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class OrderEventManager {
    private val _events = MutableSharedFlow<OrderEvent>()
    val events: SharedFlow<OrderEvent> = _events.asSharedFlow()

    suspend fun sendEvent(event: OrderEvent) {
        _events.emit(event)
    }
}