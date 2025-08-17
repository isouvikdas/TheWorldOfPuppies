package com.example.theworldofpuppies.services.grooming.domain

import com.example.theworldofpuppies.core.domain.util.Error

data class GroomingUiState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val grooming: Grooming? = null,
    val subServices: List<SubService> = mutableListOf()
)
