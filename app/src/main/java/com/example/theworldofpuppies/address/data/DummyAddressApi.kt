package com.example.theworldofpuppies.address.data

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressType
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

class DummyAddressApi {
    val addresses = listOf<Address>(
        Address(
            id = 1,
            addressType = AddressType.HOME,
            contactNumber = "+919876543210",
            contactName = "Souvik Das",
            houseNumber = "123B",
            street = "MG Road",
            landmark = "Near City Mall",
            city = "Agartala",
            state = "Tripura",
            pinCode = "799003",
            country = "India"
        ),
        Address(
            id = 2,
            addressType = AddressType.OFFICE,
            contactNumber = "+919876543210",
            contactName = "Souvik Das",
            houseNumber = "123B",
            street = "MG Road",
            landmark = "Near City Mall",
            city = "Agartala",
            state = "Tripura",
            pinCode = "799003",
            country = "India"
        )
    )

    fun getAddresses(): Result<List<Address>, NetworkError> {
        return Result.Success(addresses)
    }
}