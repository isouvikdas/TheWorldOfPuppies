package com.example.theworldofpuppies.core.presentation

data class SearchUiState<T>(
    val query: String = "",
    val results: List<T> = emptyList(),
    val isSearching: Boolean = false
)
