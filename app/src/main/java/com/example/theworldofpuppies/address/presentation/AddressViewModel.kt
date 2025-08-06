package com.example.theworldofpuppies.address.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressDetailUiState
import com.example.theworldofpuppies.address.domain.AddressRepository
import com.example.theworldofpuppies.address.domain.AddressType
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.util.normalizeIndianPhoneNumber
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddressViewModel(
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _addressUiState = MutableStateFlow(AddressUiState())
    val addressUiState: StateFlow<AddressUiState> = _addressUiState.asStateFlow()

    private val _addressDetailUiState = MutableStateFlow(AddressDetailUiState())
    val addressDetailUiState: StateFlow<AddressDetailUiState> = _addressDetailUiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    init {
        getAddresses()
    }

    fun onEditAddressClick(navController: NavController, address: Address) {
        viewModelScope.launch {
            updateContactName(address.contactName)
            updateHouseNumber(address.houseNumber)
            updateLandmark(address.landmark)
            updateStreet(address.street)
            updateCity(address.city)
            updateContactNumber(address.contactNumber)
            updatePinCode(address.pinCode)
            _addressDetailUiState.update {
                it.copy(
                    originalAddress = address,
                    selectedAddressType = address.addressType,
                    isNewAddress = false,
                    id = address.id,
                    contactName = address.contactName,
                    contactNumber = address.contactNumber,
                    houseNumber = address.houseNumber,
                    street = address.street,
                    landmark = address.landmark,
                    city = address.city,
                    state = address.state,
                    pinCode = address.pinCode,
                    country = address.country,
                )
            }
            navController.navigate(Screen.AddressDetailScreen.route)
        }
    }

    fun selectAddressType(addressType: AddressType) {
        viewModelScope.launch {
            _addressDetailUiState.update { it.copy(selectedAddressType = addressType) }
        }
    }

    fun onAddAddressClick(navController: NavController) {
        viewModelScope.launch {
            updateContactName("")
            updateHouseNumber("")
            updateLandmark("")
            updateStreet("")
            updateCity("")
            updateContactNumber("")
            updatePinCode("")
            selectAddressType(AddressType.NULL)
            _addressDetailUiState.update {
                it.copy(isNewAddress = true, originalAddress = null)
            }
            navController.navigate(Screen.AddressDetailScreen.route)
        }
    }

    fun getAddresses() {
        viewModelScope.launch {
            try {
                when (val addressResult = addressRepository.getAddresses()) {
                    is Result.Success -> {
                        _addressUiState.update { it.copy(addresses = addressResult.data as MutableList<Address>) }
                    }

                    is Result.Error -> {
                        _addressUiState.update { it.copy(error = addressResult.error) }
                    }
                }
            } catch (e: Exception) {
                _addressUiState.update { it.copy(error = NetworkError.UNKNOWN) }
                println(e.message)
            } finally {
                _addressUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateAddressSelection(address: Address) {
        viewModelScope.launch {
            try {
                _addressUiState.update { it.copy(isLoading = true) }
                if (address.isSelected) {
                    _toastEvent.emit("Address already selected")
                    return@launch
                }
                when (val result = addressRepository.updateAddressSelection(address.id)) {
                    is Result.Success -> {
                        _addressUiState.update { it.copy(addresses = result.data as MutableList<Address>) }
                    }

                    is Result.Error -> {
                        _addressUiState.update { it.copy(error = result.error) }
                    }
                }
            } catch (e: Exception) {
                _addressUiState.update { it.copy(error = NetworkError.UNKNOWN) }
                println(e.message)
            } finally {
                _addressUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun addOrUpdateAddress(
        address: Address,
        navController: NavController,
    ) {
        if (_addressDetailUiState.value.isNewAddress) {
            addNewAddress(address, navController)
        } else {
            updateAddress(address, navController)
        }
    }

    fun addNewAddress(address: Address, navController: NavController) {
        viewModelScope.launch {
            try {
                if (validateFields()) {
                    val normalizedPhoneNumber = normalizeIndianPhoneNumber(address.contactNumber)
                        ?: ""
                    Log.i("contact", normalizedPhoneNumber)
                    val finalAddress = address.copy(contactNumber = normalizedPhoneNumber)
                    _addressDetailUiState.update { it.copy(isLoading = true) }
                    when (val result = addressRepository.addNewAddress(finalAddress)) {
                        is Result.Success -> {
                            val savedAddress = result.data
                            val updatedList =
                                _addressUiState.value.addresses.toMutableList()
                            updatedList.add(savedAddress)
                            _addressUiState.update { it.copy(addresses = updatedList) }
                            navigateBack(navController)
                        }

                        is Result.Error -> {
                            _addressUiState.update { it.copy(error = result.error) }
                        }
                    }
                } else {
                    return@launch
                }
            } catch (e: Exception) {
                _addressUiState.update { it.copy(error = NetworkError.UNKNOWN) }
                println(e.message)
            } finally {
                _addressDetailUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateAddress(address: Address, navController: NavController) {
        viewModelScope.launch {
            try {
                if (validateFields()) {
                    val normalizedPhoneNumber = normalizeIndianPhoneNumber(address.contactNumber)
                        ?: ""
                    val originalAddress = _addressDetailUiState.value.originalAddress
                    if (!isAddressChanged(originalAddress!!)) {
                        navigateBack(navController)
                        return@launch
                    }
                    val finalAddress = address.copy(contactNumber = normalizedPhoneNumber)
                    _addressDetailUiState.update { it.copy(isLoading = true) }

                    when (val result = addressRepository.updateAddress(finalAddress)) {
                        is Result.Success -> {
                            val updatedAddress = result.data
                            val updatedAddresses = _addressUiState.value.addresses.map {
                                if (it.id == updatedAddress.id) {
                                    updatedAddress
                                } else {
                                    it
                                }
                            } as MutableList<Address>
                            _addressUiState.update { it.copy(addresses = updatedAddresses) }
                            navigateBack(navController)
                        }

                        is Result.Error -> {
                            _addressUiState.update { it.copy(error = result.error) }
                        }
                    }
                } else {
                    return@launch
                }
            } catch (e: Exception) {
                _addressUiState.update { it.copy(error = NetworkError.UNKNOWN) }
                println(e.message)
            } finally {
                _addressDetailUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            try {
                _addressUiState.update { it.copy(isLoading = true) }
                when (val result = addressRepository.deleteAddress(addressId)) {
                    is Result.Success -> {
                        _addressUiState.update { it.copy(addresses = result.data as MutableList<Address>) }
                    }
                    is Result.Error -> {
                        _addressUiState.update { it.copy(error = result.error) }
                    }
                }
            } catch (e: Exception) {
                _addressUiState.update { it.copy(error = NetworkError.UNKNOWN) }
                println(e.message)
            } finally {
                _addressUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun navigateBack(navController: NavController) {
        navController.popBackStack()
    }


    fun isAddressChanged(original: Address): Boolean {
        val current = addressDetailUiState.value

        return original.contactName != current.contactName ||
                original.contactNumber != current.contactNumber ||
                original.houseNumber != current.houseNumber ||
                original.landmark != current.landmark ||
                original.street != current.street ||
                original.city != current.city ||
                original.pinCode != current.pinCode
    }

    fun validateFields(): Boolean {
        var isValid = true

        val contactNameError = if (addressDetailUiState.value.contactName.isBlank()) {
            isValid = false
            "Name cannot be empty"
        } else null

        val contactNumberError = if (addressDetailUiState.value.contactNumber.isBlank()) {
            isValid = false
            "Contact number is required"
        } else if (normalizeIndianPhoneNumber(addressDetailUiState.value.contactNumber) == null) {
            isValid = false
            "Invalid contact number"
        } else null

        val cityError = if (addressDetailUiState.value.city.isBlank()) {
            isValid = false
            "City is required"
        } else null

        val pinCodeError = if (addressDetailUiState.value.pinCode.isBlank()) {
            isValid = false
            "Pin code is required"
        } else null

        val landmarkError = if (addressDetailUiState.value.landmark.isBlank()) {
            isValid = false
            "Pin code is required"
        } else null

        val streetError = if (addressDetailUiState.value.street.isBlank()) {
            isValid = false
            "Pin code is required"
        } else null

        _addressDetailUiState.update {
            it.copy(
                contactNameError = contactNameError,
                contactNumberError = contactNumberError,
                cityError = cityError,
                pinCodeError = pinCodeError,
                landmarkError = landmarkError,
                streetError = streetError

            )
        }

        return isValid
    }

    fun updateContactName(value: String) {
        _addressDetailUiState.update {
            it.copy(
                contactName = value,
                contactNameError = null
            )
        }
    }

    fun updateContactNumber(value: String) {
        _addressDetailUiState.update {
            it.copy(
                contactNumber = value,
                contactNumberError = null
            )
        }
    }

    fun updateLandmark(value: String) {
        _addressDetailUiState.update {
            it.copy(
                landmark = value,
                landmarkError = null
            )
        }
    }

    fun updateStreet(value: String) {
        _addressDetailUiState.update {
            it.copy(
                street = value,
                streetError = null
            )
        }
    }

    fun updateCity(value: String) {
        _addressDetailUiState.update {
            it.copy(
                city = value,
                cityError = null
            )
        }
    }

    fun updatePinCode(value: String) {
        _addressDetailUiState.update {
            it.copy(
                pinCode = value,
                pinCodeError = null
            )
        }
    }

    fun updateHouseNumber(value: String) {
        _addressDetailUiState.update {
            it.copy(
                houseNumber = value
            )
        }
    }


}