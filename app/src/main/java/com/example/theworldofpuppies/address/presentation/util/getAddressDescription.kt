package com.example.theworldofpuppies.address.presentation.util

import com.example.theworldofpuppies.address.domain.Address

fun getAddressDescription(address: Address): String {
    val addressDescription = if (address.houseNumber.isBlank()) {
        "${address.landmark}, ${address.street}, ${address.city}, ${address.state}, ${address.pinCode}"
    } else {
        "${address.houseNumber}, ${address.landmark}, ${address.street}, ${address.city}, ${address.state}, ${address.pinCode}"
    }
    return addressDescription
}