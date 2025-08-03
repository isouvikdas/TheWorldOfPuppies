package com.example.theworldofpuppies.address.domain

data class Address(
    val id: Int,
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