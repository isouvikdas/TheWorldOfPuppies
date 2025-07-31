package com.example.theworldofpuppies.shop.order.presentation.utils

import com.example.theworldofpuppies.core.domain.util.NetworkError

sealed interface OrderEvent {
    data class Error(val error: NetworkError): OrderEvent
    object orderPlaced : OrderEvent
}