package com.example.theworldofpuppies.shop.order.presentation.utils

import com.example.theworldofpuppies.core.domain.util.NetworkError

sealed class OrderEvent {
    data class OrderFailed(val error: NetworkError): OrderEvent()
    data class OrderConfirmed(val orderId: String) : OrderEvent()
}