package com.example.theworldofpuppies.refer_earn.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError

data class ReferEarnUiState(
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null,
    val walletBalance: Double = 0.0,
    val referralCode: String = "",
    val isRefreshing: Boolean = false
)
