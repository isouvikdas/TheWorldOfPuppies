package com.example.theworldofpuppies.address.data.networking

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class AddressApi(
    private val httpClient: HttpClient
) {

    suspend fun getAddresses(token: String):
            Result<ApiResponse<List<AddressDto>>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("auth/address/get")
            ) {
                header("Authorization", token)
            }
        }
    }

    suspend fun addNewAddress(token: String, address: AddressDto):
            Result<ApiResponse<AddressDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("auth/address/add")
            ) {
                header("Authorization", token)
                setBody(address)
            }
        }
    }

    suspend fun updateAddress(token: String, address: AddressDto):
            Result<ApiResponse<AddressDto>, NetworkError> {
        return safeCall {
            httpClient.put(
                urlString = constructUrl("auth/address/update")
            ) {
                header("Authorization", token)
                setBody(address)
            }
        }
    }

    suspend fun updateAddressSelection(token: String, addressId: String):
            Result<ApiResponse<List<AddressDto>>, NetworkError> {
        return safeCall {
            httpClient.put(
                urlString = constructUrl("auth/address/update-selection")
            ) {
                header("Authorization", token)
                parameter("addressId", addressId)
            }
        }
    }

    suspend fun deleteAddress(token: String, addressId: String):
            Result<ApiResponse<List<AddressDto>>, NetworkError> {
        return safeCall {
            httpClient.delete(
                urlString = constructUrl("auth/address/delete")
            ) {
                header("Authorization", token)
                parameter("addressId", addressId)
            }
        }
    }
}