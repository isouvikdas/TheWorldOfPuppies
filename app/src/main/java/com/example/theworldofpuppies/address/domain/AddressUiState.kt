package com.example.theworldofpuppies.address.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError

data class AddressUiState(
    val addresses: List<Address> = emptyList(),
    val isLoading: Boolean = false,
    val error: NetworkError? = null,
)
