package com.example.theworldofpuppies.address.data.dto

import com.example.theworldofpuppies.address.domain.AddressType
import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val id: String,
    val addressType: AddressType,
    val contactNumber: String,
    val contactName: String,
    val houseNumber: String,
    val street: String,
    val landmark: String,
    val city: String,
    val state: String,
    val pinCode: String,
    val country: String,
    val isSelected: Boolean = false
)
