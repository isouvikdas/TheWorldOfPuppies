package com.example.theworldofpuppies.services.grooming.domain

import com.example.theworldofpuppies.core.domain.util.Error
import com.example.theworldofpuppies.core.domain.util.NetworkError

data class GroomingUiState(
    val isLoading: Boolean = false,
    val error: NetworkError? = null,
    val grooming: Grooming? = null,
    val subServices: List<GroomingSubService> = mutableListOf(),
    val selectedSubServiceId: String = ""
)
