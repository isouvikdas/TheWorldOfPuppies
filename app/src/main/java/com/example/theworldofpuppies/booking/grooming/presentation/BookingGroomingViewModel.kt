package com.example.theworldofpuppies.booking.grooming.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.booking.grooming.domain.BookingGroomingRepository
import com.example.theworldofpuppies.booking.grooming.domain.GroomingBookingUiState
import com.example.theworldofpuppies.booking.grooming.domain.GroomingSlot
import com.example.theworldofpuppies.booking.grooming.domain.GroomingSlotWithDate
import com.example.theworldofpuppies.booking.grooming.domain.GroomingTimeUiState
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.grooming.domain.GroomingSubService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.collections.emptyList

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

    fun onDateSelect(date: LocalDateTime) {
        viewModelScope.launch {
            val timeSlots = _groomingTimeUiState.value.timeSlots
            val slotPerDate = getSlotPerDate(selectedDate = date, timeSlots = timeSlots)
            _groomingTimeUiState.update {
                it.copy(
                    selectedDate = date,
                    currentTime = LocalDateTime.now(),
                    selectedSlot = null,
                    slotPerDate = slotPerDate
                )
            }
        }
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
                when (val result = bookingGroomingRepository.getGroomingTimeSlots()) {
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
                                filterDateTime(slots = slots)
                            val selectedDate = _groomingTimeUiState.value.selectedDate
                            val slotPerDate = getSlotPerDate(selectedDate = selectedDate, timeSlots = timeSlotsWithDate)

                            _groomingTimeUiState.update {
                                it.copy(
                                    timeSlots = timeSlotsWithDate,
                                    slotPerDate = slotPerDate
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
                        isLoading = false
                    )
                }
            }
        }
    }

    fun placeBooking(
        petId: String,
        serviceId: String,
        subService: GroomingSubService?,
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
                    selectedAddress = selectedAddress,
                    serviceId = serviceId
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

    private fun filterDateTime(slots: List<GroomingSlot>): List<GroomingSlotWithDate> {
        return slots
            .groupBy { it.startTime.toLocalDate() }  // group by date
            .map { (date, slotsForDate) ->
                GroomingSlotWithDate(
                    date = date.atStartOfDay(),  // you said date: LocalDateTime
                    slots = slotsForDate.sortedBy { it.startTime } // sort within date
                )
            }
            .sortedBy { it.date } // sort outer list by date
    }

    private fun getSlotPerDate(selectedDate: LocalDateTime, timeSlots: List<GroomingSlotWithDate>): List<GroomingSlot>{
        val filteredSlot = timeSlots.find { it.date.toLocalDate() == selectedDate.toLocalDate() }?.slots ?: emptyList()
        return filteredSlot
    }
}
