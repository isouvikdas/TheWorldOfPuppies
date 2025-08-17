package com.example.theworldofpuppies.address.data

import com.example.theworldofpuppies.address.data.mappers.toAddress
import com.example.theworldofpuppies.address.data.mappers.toAddressDto
import com.example.theworldofpuppies.address.data.remote.AddressApi
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressRepository
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

class AddressRepositoryImpl(
    private val addressApi: AddressApi,
    private val userRepository: UserRepository
) : AddressRepository {

    override suspend fun getAddresses(): Result<List<Address>, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return when (val result = addressApi.getAddresses(token)) {
            is Result.Success -> {
                val response = result.data
                if (!response.success || response.data == null) {
                    Result.Error(NetworkError.SERVER_ERROR)
                } else {
                    val addresses = response.data.map { it.toAddress() }
                    Result.Success(addresses)
                }
            }
            is Result.Error -> {
                Result.Error(NetworkError.UNKNOWN)
            }
        }

    }

    override suspend fun updateAddressSelection(addressId: String): Result<List<Address>, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return when (val result = addressApi.updateAddressSelection(token, addressId)) {
            is Result.Success -> {
                val response = result.data
                if (!response.success || response.data == null) {
                    Result.Error(NetworkError.SERVER_ERROR)
                } else {
                    Result.Success(response.data.map { it.toAddress() })
                }
            }
            is Result.Error -> {
                Result.Error(NetworkError.UNKNOWN)
            }
        }
    }

    override suspend fun updateAddress(address: Address): Result<Address, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return when (val result = addressApi.updateAddress(token, address.toAddressDto())) {
            is Result.Success -> {
                val response = result.data
                if (!response.success || response.data == null) {
                    Result.Error(NetworkError.SERVER_ERROR)
                } else {
                    Result.Success(response.data.toAddress())
                }
            }
            is Result.Error -> {
                Result.Error(NetworkError.UNKNOWN)
            }
        }
    }

    override suspend fun addNewAddress(address: Address): Result<Address, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return when (val result = addressApi.addNewAddress(token, address.toAddressDto())) {
            is Result.Success -> {
                val response = result.data
                if (!response.success || response.data == null) {
                    Result.Error(NetworkError.SERVER_ERROR)
                } else {
                    Result.Success(response.data.toAddress())
                }
            }
            is Result.Error -> {
                Result.Error(NetworkError.UNKNOWN)
            }
        }
    }

    override suspend fun deleteAddress(
        addressId: String
    ): Result<List<Address>, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return when (val result = addressApi.deleteAddress(token, addressId)) {
            is Result.Success -> {
                val response = result.data
                if (!response.success || response.data == null) {
                    Result.Error(NetworkError.SERVER_ERROR)
                } else {
                    Result.Success(response.data.map { it.toAddress() })
                }
            }
            is Result.Error -> {
                Result.Error(NetworkError.UNKNOWN)
            }
        }
    }
}