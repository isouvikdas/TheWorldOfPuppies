package com.example.theworldofpuppies.services.grooming.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.Event
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.grooming.domain.GroomingRepository
import com.example.theworldofpuppies.services.grooming.domain.GroomingUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.stream.Collectors

class GroomingViewModel(
    private val groomingRepository: GroomingRepository,
    private val authEventManager: AuthEventManager
) : ViewModel() {

    private val _groomingUiSate = MutableStateFlow(GroomingUiState())
    val groomingUiState = _groomingUiSate.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    init {
        observeAuthEvents()
    }

    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun onBookNowClick(navController: NavController) {
        viewModelScope.launch {
            if (groomingUiState.value.selectedSubServiceId.isNotEmpty()) {
                navController.navigate(Screen.BookingGroomingScreen.route)
            } else {
                showToast("Please select a service")
                return@launch
            }
        }
    }

    fun loadGrooming(forceRefresh: Boolean = false) {
        val state = groomingUiState.value
        if (forceRefresh || (!state.isLoading && state.grooming == null)) {
            getGrooming()
        }
    }

    fun selectSubService(subServiceId: String) {
        _groomingUiSate.update { it.copy(selectedSubServiceId = subServiceId) }
    }

    fun getGrooming() {
        viewModelScope.launch {
            if (groomingUiState.value.isLoading) return@launch
            Log.i("grooming", "grooming")
            _groomingUiSate.update { it.copy(isLoading = true, error = null) }
            try {
                when (val result = groomingRepository.getGrooming()) {
                    is Result.Success -> {
                        val subServices = result.data.subServices
                        val isAlreadyThere =
                            subServices.any { it.id == groomingUiState.value.selectedSubServiceId }
                        if (!isAlreadyThere) {
                            selectSubService("")
                        }
                        _groomingUiSate.update {
                            val discount = result.data.discount
                            val subServices = subServices
                            val updatedServices = subServices.stream()
                                .map { service ->
                                    service.copy(
                                        discountedPrice = calculateDiscountedPrice(
                                            service.price,
                                            discount
                                        )
                                    )
                                }.collect(Collectors.toList())
                            it.copy(
                                grooming = result.data,
                                subServices = updatedServices
                            )
                        }
                    }

                    is Result.Error -> {
                        _groomingUiSate.update { it.copy(error = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("grooming service fetching error", e.toString())
                _groomingUiSate.update { it.copy(error = NetworkError.UNKNOWN) }
            } finally {
                _groomingUiSate.update { it.copy(isLoading = false) }
            }
        }


    }

    fun calculateDiscountedPrice(price: Double, discount: Int): Double {
        return price * (100 - discount) / 100
    }

    fun observeAuthEvents() {
        viewModelScope.launch {
            authEventManager.events.collect { event ->
                if (event is Event.LoggedIn) {
                    _groomingUiSate.update { GroomingUiState() }
                    getGrooming()
                }
            }
        }
    }

}