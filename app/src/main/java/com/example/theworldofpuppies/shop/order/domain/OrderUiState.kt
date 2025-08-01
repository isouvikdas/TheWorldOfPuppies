package com.example.theworldofpuppies.shop.order.domain

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.core.domain.util.Error
import com.example.theworldofpuppies.shop.order.data.response.PaymentResponse

data class OrderUiState(
    val orderId: String? = null,
    val isLoading: Boolean = false,
    val error: Error? = null,
    val showSuccessDialog: Boolean = false,
    val paymentResponse: PaymentResponse? = null,
    val addresses: MutableList<Address> = mutableListOf(),
    val selectedAddress: Address? = null,
)