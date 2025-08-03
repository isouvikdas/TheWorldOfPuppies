package com.example.theworldofpuppies.address.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressRepository
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddressViewModel(private val addressRepository: AddressRepository) : ViewModel() {

    private val _addressUiState = MutableStateFlow(AddressUiState())
    val addressUiState: StateFlow<AddressUiState> = _addressUiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    init {
        getAddresses()
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
                        _addressUiState.update { it.copy(addresses = result as MutableList<Address>) }
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
}