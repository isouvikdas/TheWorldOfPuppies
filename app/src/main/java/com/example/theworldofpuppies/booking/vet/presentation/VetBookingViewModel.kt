package com.example.theworldofpuppies.booking.vet.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.booking.vet.domain.VetBookingRepository
import com.example.theworldofpuppies.booking.vet.domain.VetBookingUiState
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VetBookingViewModel(
    private val vetBookingRepository: VetBookingRepository
) : ViewModel() {

    private val _vetBookingUiState = MutableStateFlow(VetBookingUiState())
    val vetBookingUiState = _vetBookingUiState.asStateFlow()

    fun onAddressChangeClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun createBooking(
        serviceId: String?,
        petId: String,
        healthIssues: List<HealthIssue>,
        healthIssueDescription: String,
        vetTimeSlot: VetTimeSlot?,
        vetOptionId: String?,
        context: Context,
    ) {
        viewModelScope.launch {
            _vetBookingUiState.update { it.copy(isLoading = true) }
            try {

                Log.i("booking1", serviceId ?: "null")
                when (val result = vetBookingRepository.createVetBooking(
                    serviceId = serviceId,
                    petId = petId,
                    healthIssues = healthIssues,
                    healthIssueDescription = healthIssueDescription,
                    vetTimeSlot = vetTimeSlot,
                    vetOptionId = vetOptionId
                )) {
                    is Result.Success -> {
                        Log.i("booking1", result.data.toString())
                        _vetBookingUiState.update {
                            it.copy(
                                isLoading = false,
                                vetBooking = result.data,
                                showSuccessDialog = true
                            )
                        }
                    }

                    is Result.Error -> {
                        _vetBookingUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.error.toString(context)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _vetBookingUiState.update {
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
        _vetBookingUiState.update { it.copy(showSuccessDialog = false) }
        if (navigationEnabled) {
            navController.navigate(route) {
                popUpTo(popUpToRoute) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    fun resetUiState() {
        viewModelScope.launch {
            _vetBookingUiState.value = VetBookingUiState()

        }
    }
}