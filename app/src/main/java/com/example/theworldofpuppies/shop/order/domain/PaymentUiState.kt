package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError

sealed class PaymentUiState {
    object Idle : PaymentUiState()
    object Loading : PaymentUiState()
    data class Success(val message: String) : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
}