package com.example.theworldofpuppies.address.domain

import com.example.theworldofpuppies.core.domain.util.Error

data class AddressDetailUiState(
    val originalAddress: Address? = null,
    val isLoading: Boolean = false,
    val error: Error? = null,
    val addressTypeList: List<AddressType> = listOf(
        AddressType.HOME,
        AddressType.OFFICE,
        AddressType.OTHER
    ),
    val selectedAddressType: AddressType = AddressType.NULL,
    val isNewAddress: Boolean = false,

    //  Field values (optional — useful if you want two-way binding with ViewModel)
    val id: String = "",
    val contactName: String = "",
    val contactNumber: String = "",
    val houseNumber: String = "",
    val landmark: String = "",
    val street: String = "",
    val city: String = "",
    val pinCode: String = "",
    val state: String = "West Bengal",
    val country: String = "India",
    val addressType: AddressType = AddressType.NULL,

    //  Validation error states
    val contactNameError: String? = null,
    val contactNumberError: String? = null,
    val cityError: String? = null,
    val pinCodeError: String? = null,
    val streetError: String? = null,
    val landmarkError: String? = null,
)

