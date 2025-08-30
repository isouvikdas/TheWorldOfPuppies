package com.example.theworldofpuppies.services.pet_walking.domain

import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import java.time.LocalDateTime

data class PetWalkingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val frequencies: List<Frequency> = listOf(Frequency.ONE_TIME, Frequency.REPEAT_WEEKLY),
    val selectedFrequency: Frequency? = Frequency.REPEAT_WEEKLY,
    val selectedDays: List<Days> = emptyList(),
    val dateRange: PetWalkDateRange? = null,
    val singleDate: LocalDateTime? = null,
    val days: List<Days> = Days.entries
)