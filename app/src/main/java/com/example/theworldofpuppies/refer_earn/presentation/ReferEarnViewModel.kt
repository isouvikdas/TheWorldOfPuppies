package com.example.theworldofpuppies.refer_earn.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.Event
import com.example.theworldofpuppies.refer_earn.domain.ReferEarnRepository
import com.example.theworldofpuppies.refer_earn.domain.ReferEarnUiState
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReferEarnViewModel(
    private val referEarnRepository: ReferEarnRepository,
    private val authEventManager: AuthEventManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _referEarnUiState = MutableStateFlow(ReferEarnUiState())
    val referEarnUiState = _referEarnUiState.asStateFlow()

    init {
        observeAuthEvents()
        if (!userRepository.getUserId().isNullOrEmpty()) {
            loadReferralAndWallet()
        }
    }

    private fun loadReferralAndWallet() {
        viewModelScope.launch {
            val cachedReferral = userRepository.getReferralCode()
            val cachedWallet = userRepository.getWalletBalance()
            Log.i("referral", "viewmodel: $cachedReferral")

            if (!cachedReferral.isNullOrEmpty()) {
                // ✅ Use cached data directly
                _referEarnUiState.update {
                    it.copy(
                        referralCode = cachedReferral,
                    )
                }
            } else if (cachedWallet > 0.0) {
                _referEarnUiState.update {
                    it.copy(
                        walletBalance = cachedWallet
                    )
                }
            }
                else {
                // 🔄 Fallback: fetch from network
                if (cachedReferral.isNullOrEmpty()) getReferralCode()
                if (cachedWallet == 0.0) getWalletBalance()
            }
        }
    }

    fun forceLoadReferralCodeAndWalletBalance() {
        viewModelScope.launch {
            _referEarnUiState.update { it.copy(isRefreshing = true) }
            getReferralCode()
            getWalletBalance()
            delay(1500)
            _referEarnUiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun getReferralCode() {
        viewModelScope.launch {
            try {
                _referEarnUiState.update { it.copy(isLoading = false, errorMessage = null) }
                when (val result = referEarnRepository.getReferralCode()) {
                    is Result.Success -> {
                        userRepository.saveReferralCode(result.data)
                        delay(500)
                        _referEarnUiState.update {
                            it.copy(
                                referralCode = userRepository.getReferralCode() ?: ""
                            )
                        }
                    }

                    is Result.Error -> {
                        _referEarnUiState.update {
                            it.copy(
                                errorMessage = result.error
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("refer_error: ", e.message.toString())
            } finally {
                _referEarnUiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun getWalletBalance() {
        viewModelScope.launch {
            try {
                _referEarnUiState.update { it.copy(isLoading = true, errorMessage = null) }
                when (val result = referEarnRepository.getWalletBalance()) {
                    is Result.Success -> {
                        _referEarnUiState.update {
                            it.copy(
                                walletBalance = result.data
                            )
                        }
                        userRepository.saveWalletBalance(result.data)
                    }

                    is Result.Error -> {
                        _referEarnUiState.update {
                            it.copy(
                                errorMessage = result.error
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("wallet_error", e.message.toString())
            } finally {
                _referEarnUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun observeAuthEvents() {
        viewModelScope.launch {
            authEventManager.events.collect { event ->
                if (event is Event.LoggedIn) {
                    loadReferralAndWallet()
                }
            }
        }
    }

}