package com.example.theworldofpuppies.services.vet.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import com.example.theworldofpuppies.services.vet.domain.VetOption
import com.example.theworldofpuppies.services.vet.domain.VetRepository
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot
import com.example.theworldofpuppies.services.vet.domain.VetUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class VetViewModel(
    private val vetRepository: VetRepository
) : ViewModel() {

    private val _vetUiState = MutableStateFlow(VetUiState())
    val vetUiState: StateFlow<VetUiState> = _vetUiState.asStateFlow()

    fun onBookNowClick(navController: NavController) {
        navController.navigate(Screen.VetIssuesScreen.route)
    }

    fun onHealthIssueDescriptionChange(value: String) {
        _vetUiState.update { it.copy(healthIssueDescription = value) }
    }

    fun onHealthIssueSelect(healthIssue: HealthIssue) {
        _vetUiState.update {
            it.copy(
                selectedHealthIssues = if (it.selectedHealthIssues.contains(healthIssue)) {
                    it.selectedHealthIssues - healthIssue
                } else {
                    it.selectedHealthIssues + healthIssue
                }
            )
        }
    }

    fun onVetOptionSelect(vetOption: VetOption) {
        viewModelScope.launch {
            _vetUiState.update {
                it.copy(
                    isLoading = true,
                    selectedVetOption = vetOption,
                    )
            }
            delay(1000)
            val selectedDate = vetUiState.value.selectedDate
            val slotsPerDate = getSlotsPerDate(selectedDate, vetOption)
            _vetUiState.update {
                it.copy(
                    selectedSlot = null,
                    slotsPerDate = slotsPerDate,
                    isLoading = false
                )
            }
        }
    }

    fun onDateSelect(date: LocalDateTime) {
        viewModelScope.launch {
            _vetUiState.update {
                it.copy(
                    isLoading = true,
                    selectedDate = date
                )
            }
            delay(1000)
            val selectedVetOption = vetUiState.value.selectedVetOption
            val slotsPerDate = getSlotsPerDate(date, selectedVetOption)
            _vetUiState.update {
                it.copy(
                    selectedSlot = null,
                    slotsPerDate = slotsPerDate,
                    isLoading = false
                )
            }
        }
    }

    fun onTimeSlotSelection(timeSlot: VetTimeSlot) {
        viewModelScope.launch {
            _vetUiState.update { it.copy(selectedSlot = timeSlot) }
        }
    }

    fun getVet(context: Context) {
        viewModelScope.launch {
            _vetUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                when (val result = vetRepository.getVet()) {
                    is Result.Success -> {
                        val vet = result.data
                        _vetUiState.update {
                            it.copy(
                                timeSlots = vet.vetTimeSlots,
                                vetOptions = vet.vetOptions,
                                name = vet.name,
                                description = vet.description,
                                discount = vet.discount,
                                category = vet.category,
                                active = vet.active,
                                id = vet.id,
                                healthIssues = vet.healthIssues
                            )
                        }
                    }

                    is Result.Error -> {
                        _vetUiState.update { it.copy(errorMessage = result.error.toString(context)) }
                    }
                }
            } catch (e: Exception) {
                _vetUiState.update { it.copy(errorMessage = e.message.toString()) }
            } finally {
                _vetUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun getSlotsPerDate(
        selectedDate: LocalDateTime,
        selectedVetOption: VetOption?
    ): List<VetTimeSlot> {
        return vetUiState.value.timeSlots
            .filter { slot ->
                slot.vetBookingCategory == selectedVetOption?.vetBookingCategory &&
                        slot.dateTime.toLocalDate() == selectedDate.toLocalDate()
            }
            .sortedBy { it.dateTime }

    }
}
