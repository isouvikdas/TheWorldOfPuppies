package com.example.theworldofpuppies.services.vet.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.review.presentation.utils.ReviewEvent
import com.example.theworldofpuppies.review.presentation.utils.ReviewEventManager
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import com.example.theworldofpuppies.services.vet.domain.VetOption
import com.example.theworldofpuppies.services.vet.domain.VetRepository
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot
import com.example.theworldofpuppies.services.vet.domain.VetUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun onProceedClick(navController: NavController) {
        val selectedVetOption = vetUiState.value.selectedVetOption
        val selectedSlot = vetUiState.value.selectedSlot
        viewModelScope.launch {
            if (selectedSlot == null) {
                showToast("Please select a time slot")
                return@launch
            }
            if (selectedVetOption == null) {
                showToast("Please select a vet option")
                return@launch
            }
            navController.navigate(Screen.PetListScreen.route)
        }
    }

    fun onSaveIssueSelect(navController: NavController) {
        viewModelScope.launch {
            if (vetUiState.value.selectedHealthIssues.isEmpty()) {
                showToast("Please select at least an issue")
                return@launch
            }
            navController.navigate(Screen.VetBookingScreen.route)
        }
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
                    isDateSectionLoading = true,
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
                    isDateSectionLoading = false
                )
            }
        }
    }

    fun onDateSelect(date: LocalDateTime) {
        viewModelScope.launch {
            _vetUiState.update {
                it.copy(
                    isDateSectionLoading = true,
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
                    isDateSectionLoading = false
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
                                healthIssues = vet.healthIssues,
                                averageReviews = vet.averageStars,
                                totalReviews = vet.totalReviews,
                                isRated = vet.isRated
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
