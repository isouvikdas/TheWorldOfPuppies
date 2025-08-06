package com.example.theworldofpuppies.address.data.mappers

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.address.domain.Address

fun AddressDto.toAddress(): Address {
    return Address(
        id = this.id,
        contactNumber = this.contactNumber,
        contactName = this.contactName,
        houseNumber = this.houseNumber,
        street = this.street,
        landmark = this.landmark,
        city = this.city,
        state = this.state,
        country = this.country,
        pinCode = this.pinCode,
        addressType = this.addressType,
        isSelected = this.isSelected
    )
}

fun Address.toAddressDto(): AddressDto {
    return AddressDto(
        id = this.id,
        contactNumber = this.contactNumber,
        contactName = this.contactName,
        houseNumber = this.houseNumber,
        street = this.street,
        landmark = this.landmark,
        city = this.city,
        state = this.state,
        country = this.country,
        pinCode = this.pinCode,
        addressType = this.addressType,
        isSelected = this.isSelected
    )
}