package com.example.theworldofpuppies.address.data

import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

class AddressRepositoryImpl(private val addressApi: DummyAddressApi): AddressRepository{

    override fun getAddresses(): Result<List<Address>, NetworkError> {
        return addressApi.getAddresses()
    }

}