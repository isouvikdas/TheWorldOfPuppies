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
    val singleDate: LocalDateTime? = LocalDateTime.now(),
    val days: List<Days> = Days.entries,
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val basePrice: Double? = null,
    val discount: Int? = null,
    val isRated: Boolean = false,
    val totalReviews: Int = 0,
    val averageReviews: Double = 0.0
)