package com.example.theworldofpuppies.services.pet_walking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkDateRange
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkingUiState
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PetWalkingViewModel() : ViewModel() {

    private val _petWalkingUiState = MutableStateFlow(PetWalkingUiState())
    val petWalkingUiState = _petWalkingUiState.asStateFlow()

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

}