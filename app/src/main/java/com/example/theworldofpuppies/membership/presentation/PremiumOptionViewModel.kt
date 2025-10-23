package com.example.theworldofpuppies.membership.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.membership.domain.PremiumOption
import com.example.theworldofpuppies.membership.domain.PremiumOptionRepository
import com.example.theworldofpuppies.membership.domain.PremiumOptionUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PremiumOptionViewModel(
    private val premiumOptionRepository: PremiumOptionRepository
) : ViewModel() {

    private val _premiumOptionUiState = MutableStateFlow(PremiumOptionUiState())
    val premiumOptionUiState = _premiumOptionUiState.asStateFlow()

    fun selectOption(option: PremiumOption) {
        viewModelScope.launch {
            _premiumOptionUiState.update { it.copy(selectedOption = option) }
        }
    }

    fun forceLoad() {
        viewModelScope.launch {
            _premiumOptionUiState.update { it.copy(isRefreshing = true) }
            getPremium()
            delay(1500)
            _premiumOptionUiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun getPremium() {
        viewModelScope.launch {
            try {
                _premiumOptionUiState.update { it.copy(isLoading = true, errorMessage = null) }
                when (val result = premiumOptionRepository.getPremiumOptions()) {
                    is Result.Success -> {
                        _premiumOptionUiState.update { it.copy(premiumOptions = result.data) }
                    }
                    is Result.Error -> {
                        _premiumOptionUiState.update { it.copy(errorMessage = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("premium error",e.message.toString())
            } finally {
                _premiumOptionUiState.update { it.copy(isLoading = false, isAlreadyLoaded = true) }
            }
        }
    }
}