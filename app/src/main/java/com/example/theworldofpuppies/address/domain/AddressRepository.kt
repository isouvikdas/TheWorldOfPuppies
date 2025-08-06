package com.example.theworldofpuppies.address.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface AddressRepository {
    suspend fun getAddresses(): Result<List<Address>, NetworkError>
    suspend fun updateAddressSelection(addressId: String): Result<List<Address>, NetworkError>

    suspend fun updateAddress(address: Address): Result<Address, NetworkError>

    suspend fun addNewAddress(address: Address): Result<Address, NetworkError>

    suspend fun deleteAddress(addressId: String): Result<List<Address>, NetworkError>
}