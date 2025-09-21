package com.example.theworldofpuppies.booking.dog_training.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBookingRepository
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBookingUIState
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingFeature
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DogTrainingBookingViewModel(
    private val dogTrainingBookingRepository: DogTrainingBookingRepository
) : ViewModel() {

    private val _dogTrainingBookingUiState = MutableStateFlow(DogTrainingBookingUIState())
    val dogTrainingBookingUiState = _dogTrainingBookingUiState.asStateFlow()

    fun onAddressChangeClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun createBooking(
        serviceId: String?,
        petId: String,
        notes: String = "",
        dogTrainingOption: DogTrainingOption?,
        dogTrainingFeatures: List<DogTrainingFeature>,
        context: Context,
    ) {
        viewModelScope.launch {
            _dogTrainingBookingUiState.update { it.copy(isLoading = true) }
            try {

                Log.i("booking1", serviceId ?: "null")
                when (val result = dogTrainingBookingRepository.createBooking(
                    serviceId = serviceId ?: "",
                    petId = petId,
                    notes = notes,
                    dogTrainingOption = dogTrainingOption,
                    dogTrainingFeatures = dogTrainingFeatures,
                    )
                ) {
                    is Result.Success -> {
                        Log.i("booking1", result.data.toString())
                        _dogTrainingBookingUiState.update {
                            it.copy(
                                isLoading = false,
                                dogTrainingBooking = result.data,
                                showSuccessDialog = true
                            )
                        }
                    }

                    is Result.Error -> {
                        _dogTrainingBookingUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.error.toString(context)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _dogTrainingBookingUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.toString()
                    )
                }
            }
        }
    }


    fun dismissDialog(
        navController: NavController,
        route: String,
        popUpToRoute: String = Screen.ProductListScreen.route,
        navigationEnabled: Boolean = true
    ) {
        _dogTrainingBookingUiState.update { it.copy(showSuccessDialog = false) }
        if (navigationEnabled) {
            navController.navigate(route) {
                popUpTo(popUpToRoute) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    fun resetUiState() {
        viewModelScope.launch {
            _dogTrainingBookingUiState.value = DogTrainingBookingUIState()
        }
    }
}