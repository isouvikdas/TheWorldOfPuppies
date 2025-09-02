package com.example.theworldofpuppies.services.pet_walking.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkDateRange
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkRepository
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkingUiState
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PetWalkingViewModel(
    private val petWalkRepository: PetWalkRepository
) : ViewModel() {

    private val _petWalkingUiState = MutableStateFlow(PetWalkingUiState())
    val petWalkingUiState = _petWalkingUiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun getPetWalking(context: Context) {
        viewModelScope.launch {
            _petWalkingUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                when (val result = petWalkRepository.getPetWalk()) {
                    is Result.Success -> {
                        _petWalkingUiState.update {
                            it.copy(
                                description = result.data.description,
                                basePrice = result.data.basePrice,
                                discount = result.data.discount,
                                name = result.data.name
                            )
                        }
                    }
                    is Result.Error -> {
                        _petWalkingUiState.update {
                            it.copy(
                                errorMessage = result.error.toString(context)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _petWalkingUiState.update {
                    it.copy(
                        errorMessage = e.message
                    )
                }
            } finally {
                _petWalkingUiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun onDaySelect(day: Days) {
        viewModelScope.launch {
            _petWalkingUiState.update {
                it.copy(
                    selectedDays = it.selectedDays.toMutableList().apply {
                        if (contains(day))
                            remove(day)
                        else
                            add(day)
                    }
                )
            }
        }
    }

    fun onStartDateSelect(date: LocalDateTime) {
        _petWalkingUiState.update { current ->
            current.copy(
                dateRange = current.dateRange?.copy(startDate = date)
                    ?: PetWalkDateRange(startDate = date, endDate = null)
            )
        }
    }

    fun onEndDateSelect(date: LocalDateTime) {
        _petWalkingUiState.update { current ->
            current.copy(
                dateRange = current.dateRange?.copy(endDate = date)
                    ?: PetWalkDateRange(startDate = null, endDate = date)
            )
        }
    }


    fun onSingleDateSelect(singleDate: LocalDateTime) {
        viewModelScope.launch {
            _petWalkingUiState.update {
                it.copy(
                    singleDate = singleDate
                )
            }
        }
    }

    fun onFrequencySelect(frequency: Frequency) {
        viewModelScope.launch {
            _petWalkingUiState.update {
                it.copy(
                    selectedFrequency = frequency
                )
            }
        }
    }

    fun onEverythingSelected() {
        _petWalkingUiState.update {
            it.copy(
                selectedDays = if (it.selectedDays.size == it.days.size) {
                    emptyList()
                } else {
                    it.days
                }
            )
        }
    }

    fun onBookNowClick(navController: NavController) {
        viewModelScope.launch {
//            if (petWalkingUiState.value.selectedDays.isNotEmpty() || petWalkingUiState.value.singleDate != null) {
//
//                navController.navigate(Screen.BookingPetWalkScreen.route)
//            } else {
//                showToast(message = "Please select at least one date")
//            }
            navController.navigate(Screen.BookingPetWalkScreen.route)

        }

    }
}