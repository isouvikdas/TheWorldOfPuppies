package com.example.theworldofpuppies.booking.presentation.grooming

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.booking.domain.grooming.BookingGroomingRepository
import com.example.theworldofpuppies.booking.domain.grooming.GroomingBookingUiState
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlot
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlotWithDate
import com.example.theworldofpuppies.booking.domain.grooming.GroomingTimeUiState
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.grooming.domain.SubService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GroomingBookingViewModel(
    private val bookingGroomingRepository: BookingGroomingRepository
) : ViewModel() {

    private val _groomingBookingUiState = MutableStateFlow(GroomingBookingUiState())
    val groomingBookingUiState = _groomingBookingUiState.asStateFlow()
    private val _groomingTimeUiState = MutableStateFlow(GroomingTimeUiState())
    val bookingTimeUiState = _groomingTimeUiState.asStateFlow()

    fun onAddressChangeClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun dismissDialog(
        navController: NavController,
        route: String,
        popUpToRoute: String = Screen.ProductListScreen.route,
        navigationEnabled: Boolean = true
    ) {
        _groomingBookingUiState.update { it.copy(showBookingSuccessDialog = false) }
        if (navigationEnabled) {
            navController.navigate(route) {
                popUpTo(popUpToRoute) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    fun onDateSelect(date: LocalDateTime, context: Context) {
        viewModelScope.launch {
            _groomingTimeUiState.update {
                it.copy(
                    selectedDate = date,
                    currentTime = LocalDateTime.now(),
                    selectedSlot = null
                )
            }
        }
        getBookingTimeSlots(context) // refresh slots on date change
    }

    fun onTimeSlotSelection(timeSlot: GroomingSlot) {
        viewModelScope.launch {
            _groomingTimeUiState.update { it.copy(selectedSlot = timeSlot) }
        }
    }

    fun getBookingTimeSlots(context: Context) {
        viewModelScope.launch {
            _groomingTimeUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val state = _groomingTimeUiState.value
                val selectedDate = state.selectedDate
                val existingTimeSlots = state.timeSlots
                val alreadyExists = existingTimeSlots.any { it.date == selectedDate }
                if (alreadyExists) {
                    return@launch
                }
                delay(2000)
                when (val result = bookingGroomingRepository.getGroomingTimeSlots(selectedDate)) {
                    is Result.Success -> {
                        val slots = result.data
                        if (slots.isEmpty()) {
                            _groomingTimeUiState.update {
                                it.copy(
                                    errorMessage = "No slots available for the selected date"
                                )
                            }
                        } else {
                            val timeSlotsWithDate =
                                GroomingSlotWithDate(date = selectedDate, slots = slots)
                            val updatedSlots = existingTimeSlots + timeSlotsWithDate
                            _groomingTimeUiState.update {
                                it.copy(
                                    timeSlots = updatedSlots.toMutableList()
                                )
                            }
                        }
                    }

                    is Result.Error -> {
                        _groomingTimeUiState.update {
                            it.copy(
                                errorMessage = result.error.toString(
                                    context
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("BookingVM", e.message.toString())
            } finally {
                _groomingTimeUiState.update {
                    it.copy(
                        isLoading = false,
                        currentTime = LocalDateTime.now()
                    )
                }
            }
        }
    }

    fun bookGrooming(
        subService: SubService?,
        selectedSlot: GroomingSlot?,
        selectedDate: LocalDateTime?,
        selectedAddress: Address?,
        context: Context
    ) {
        viewModelScope.launch {
            _groomingBookingUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    showBookingSuccessDialog = false
                )
            }
            try {
                if (subService == null || selectedSlot == null || selectedDate == null || selectedAddress == null) {
                    return@launch
                }
                when (val result = bookingGroomingRepository.bookGrooming(
                    subService = subService,
                    selectedSlot = selectedSlot,
                    selectedDate = selectedDate,
                    selectedAddress = selectedAddress
                )) {
                    is Result.Success -> {
                        _groomingBookingUiState.update {
                            it.copy(
                                booking = result.data,
                                showBookingSuccessDialog = true
                            )
                        }
                    }

                    is Result.Error -> {
                        _groomingBookingUiState.update {
                            it.copy(errorMessage = result.error.toString(context))
                        }
                    }
                }
            } catch (e: Exception) {
                _groomingBookingUiState.update {
                    it.copy(
                        errorMessage = e.message.toString()
                    )
                }
            } finally {
                _groomingBookingUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetUiStates() {
        viewModelScope.launch {
            _groomingBookingUiState.value = GroomingBookingUiState()
            _groomingTimeUiState.value = GroomingTimeUiState()
        }
    }
}
