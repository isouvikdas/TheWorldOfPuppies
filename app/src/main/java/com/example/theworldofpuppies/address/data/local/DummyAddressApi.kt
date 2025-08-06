package com.example.theworldofpuppies.address.data.local

class DummyAddressApi {
//    private val addresses = listOf<Address>(
//        Address(
//            id = 1,
//            addressType = AddressType.HOME,
//            contactNumber = "+919876543210",
//            contactName = "Souvik Das",
//            houseNumber = "123B",
//            street = "MG Road",
//            landmark = "Near City Mall",
//            city = "Agartala",
//            state = "Tripura",
//            pinCode = "799003",
//            country = "India",
//            isSelected = true
//        ),
//        Address(
//            id = 2,
//            addressType = AddressType.OFFICE,
//            contactNumber = "+919876543210",
//            contactName = "Souvik Das",
//            houseNumber = "123B",
//            street = "MG Road",
//            landmark = "Near City Mall",
//            city = "Agartala",
//            state = "Tripura",
//            pinCode = "799003",
//            country = "India",
//            isSelected = false
//        )
//    )
//
//    fun getAddresses(): Result<List<Address>, NetworkError> {
//        return Result.Success(addresses)
//    }
//
//    fun updateAddress(address: Address, token: String): Result<ApiResponse<Address>, NetworkError> {
//            val existingAddress = addresses.find { it.id == address.id }
//            val updateAddress = existingAddress!!.copy(
//                addressType = address.addressType,
//                contactNumber = address.contactNumber,
//                contactName = address.contactName,
//                houseNumber = address.houseNumber,
//                street = address.street,
//                landmark = address.landmark,
//                city = address.city,
//                state = address.state,
//                pinCode = address.pinCode,
//                country = address.country
//            )
//            return Result.Success(ApiResponse<Address>(data = updateAddress, message = "success", success = true))
//    }
//
//    fun updateAddressSelection(addressId: Int, token: String): Result<ApiResponse<List<Address>>, NetworkError> {
//        val updatedAddresses = addresses.map { address ->
//            if (address.id == addressId) {
//                address.copy(isSelected = true)
//            } else {
//                address.copy(isSelected = false)
//            }
//        }
//        return Result.Success(ApiResponse< List<Address>>(data = updatedAddresses, message = "success", success = true))
//    }
//
//    fun addAddress(address: Address, token: String): Result<ApiResponse<Address>, NetworkError> {
//        // Simulate auto-increment ID
//        val newId = (addresses.maxOfOrNull { it.id } ?: 0) + 1
//
//        val newAddress = address.copy(
//            id = newId,
//            isSelected = false
//        )
//
//        return Result.Success(
//            ApiResponse(
//                data = newAddress,
//                message = "Address added successfully",
//                success = true
//            )
//        )
//    }



}