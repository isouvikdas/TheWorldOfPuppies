package com.example.theworldofpuppies.address.domain

import com.example.theworldofpuppies.core.domain.util.Error

data class AddressUiState(
    val addresses: MutableList<Address> = mutableListOf(),
    val selectedAddress: Address? = null,
    val isLoading: Boolean = false,
    val error: Error? = null
)
