package com.example.theworldofpuppies.address.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface AddressRepository {
    fun getAddresses(): Result<List<Address>, NetworkError>
}