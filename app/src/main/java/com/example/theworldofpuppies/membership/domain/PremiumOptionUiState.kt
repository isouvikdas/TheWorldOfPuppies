package com.example.theworldofpuppies.membership.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.shop.order.domain.PaymentMethod

data class PremiumOptionUiState(
    val premiumOptions: List<PremiumOption> = emptyList(),
    val selectedOption: PremiumOption? = null,
    val premiumOptionOrder: PremiumOptionOrder? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
    val isAlreadyLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null,
    val isRefreshing: Boolean = false,
    val checkoutLoading: Boolean = false
)
