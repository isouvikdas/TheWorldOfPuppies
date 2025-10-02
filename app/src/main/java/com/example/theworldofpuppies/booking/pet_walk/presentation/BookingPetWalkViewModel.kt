package com.example.theworldofpuppies.booking.pet_walk.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBookingRepository
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBookingUiState
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkDateRange
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.log

class BookingPetWalkViewModel(
    private val petWalkBookingRepository: PetWalkBookingRepository
) : ViewModel() {

    private val _petWalkBookingUiState = MutableStateFlow(PetWalkBookingUiState())
    val petWalkBookingUiState = _petWalkBookingUiState.asStateFlow()
    fun onAddressChangeClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun createBooking(
        serviceId: String,
        petId: String,
        notes: String = "",
        serviceDate: LocalDateTime? = null,
        dateRange: PetWalkDateRange? = null,
        frequency: Frequency,
        context: Context,
        selectedDays: List<Days>? = null
    ) {
        viewModelScope.launch {
            _petWalkBookingUiState.update { it.copy(isLoading = true) }
            try {

                Log.i("booking1", serviceId)
                when (val result = petWalkBookingRepository.createBooking(
                    serviceId = serviceId,
                    petId = petId,
                    notes = notes,
                    serviceDate = serviceDate,
                    dateRange = dateRange,
                    frequency = frequency,
                    selectedDays = selectedDays
                )) {
                    is Result.Success -> {
                        Log.i("booking1", result.data.toString())
                        _petWalkBookingUiState.update {
                            it.copy(
                                isLoading = false,
                                petWalkBooking = result.data,
                                showSuccessDialog = true
                            )
                        }
                    }
                    is Result.Error -> {
                        _petWalkBookingUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.error.toString(context)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _petWalkBookingUiState.update {
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
        _petWalkBookingUiState.update { it.copy(showSuccessDialog = false) }
        if (navigationEnabled) {
            navController.navigate(route) {
                popUpTo(popUpToRoute) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    fun resetUiState() {
        viewModelScope.launch {
            _petWalkBookingUiState.value = PetWalkBookingUiState()

        }
    }
}