package com.example.theworldofpuppies.booking.presentation.grooming

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.booking.domain.grooming.BookingRepository
import com.example.theworldofpuppies.booking.domain.grooming.BookingTimeUiState
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlot
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlotWithDate
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class BookingViewModel(
    private val bookingRepository: BookingRepository,
) : ViewModel() {

    private val _bookingTimeUiState = MutableStateFlow(BookingTimeUiState())
    val bookingTimeUiState = _bookingTimeUiState.asStateFlow()

    fun onAddressChangeClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun onDateSelect(date: LocalDate, context: Context) {
        viewModelScope.launch {
            _bookingTimeUiState.update { it.copy(
                selectedDate = date,
                currentTime = LocalTime.now(),
                selectedSlot = null
            ) }
        }
        getBookingTimeSlots(context) // refresh slots on date change
    }

    fun onTimeSlotSelection(timeSlot: GroomingSlot) {
        viewModelScope.launch {
            _bookingTimeUiState.update { it.copy(selectedSlot = timeSlot) }
        }
    }

    fun getBookingTimeSlots(context: Context) {
        viewModelScope.launch {
            _bookingTimeUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val state = _bookingTimeUiState.value
                val selectedDate = state.selectedDate
                val existingTimeSlots = state.timeSlots
                val alreadyExists = existingTimeSlots.any { it.date == selectedDate }
                if (alreadyExists) {
                    return@launch
                }
                delay(2000)
                when (val result = bookingRepository.getGroomingTimeSlots(selectedDate)) {
                    is Result.Success -> {
                        val slots = result.data
                        if (slots.isEmpty()) {
                            _bookingTimeUiState.update {
                                it.copy(
                                    errorMessage = "No slots available for the selected date"
                                )
                            }
                        } else {
                            val timeSlotsWithDate =
                                GroomingSlotWithDate(date = selectedDate, slots = slots)
                            val updatedSlots = existingTimeSlots + timeSlotsWithDate
                            _bookingTimeUiState.update {
                                it.copy(
                                    timeSlots = updatedSlots.toMutableList()
                                )
                            }
                        }
                    }

                    is Result.Error -> {
                        _bookingTimeUiState.update {
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
                _bookingTimeUiState.update {
                    it.copy(
                        isLoading = false,
                        currentTime = LocalTime.now()
                    )
                }
            }
        }
    }
}
