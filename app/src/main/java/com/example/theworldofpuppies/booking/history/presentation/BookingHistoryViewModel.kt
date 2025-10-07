package com.example.theworldofpuppies.booking.history.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.booking.core.presentation.utils.BookingEvent
import com.example.theworldofpuppies.booking.core.presentation.utils.BookingEventManager
import com.example.theworldofpuppies.booking.history.domain.BookingHistoryRepository
import com.example.theworldofpuppies.booking.history.domain.BookingHistoryUiState
import com.example.theworldofpuppies.core.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookingHistoryViewModel(
    private val bookingHistoryRepository: BookingHistoryRepository,
    private val bookingEventManager: BookingEventManager
) : ViewModel() {

    private val _bookingHistoryUiState = MutableStateFlow(BookingHistoryUiState())
    val bookingHistoryUiState: StateFlow<BookingHistoryUiState> =
        _bookingHistoryUiState.asStateFlow()

    init {
        observeBookingEvents()
    }

    fun getGroomingBookings() {
        viewModelScope.launch {
            try {
                _bookingHistoryUiState.update { it.copy(groomingLoading = true) }
                when (val result = bookingHistoryRepository.getGroomingBookings()) {
                    is Result.Success -> {
                        _bookingHistoryUiState.update {
                            it.copy(
                                groomingBookings = result.data,
                                groomingError = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _bookingHistoryUiState.update { it.copy(groomingError = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("GroomingBookings", e.toString())
            } finally {
                _bookingHistoryUiState.update { it.copy(groomingLoading = false, groomingLoaded = true) }
            }
        }
    }

    fun getDogTrainingBookings() {
        viewModelScope.launch {
            try {
                _bookingHistoryUiState.update { it.copy(dogTrainingLoading = true) }
                when (val result = bookingHistoryRepository.getDogTrainingBookings()) {
                    is Result.Success -> {
                        _bookingHistoryUiState.update {
                            it.copy(
                                dogTrainingBookings = result.data,
                                dogTrainingError = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _bookingHistoryUiState.update { it.copy(dogTrainingError = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("DogTrainingBookings", e.toString())
            } finally {
                _bookingHistoryUiState.update {
                    it.copy(
                        dogTrainingLoading = false,
                        dogTrainingLoaded = true
                    )
                }
            }
        }
    }

    fun getPetWalkBookings() {
        viewModelScope.launch {
            try {
                _bookingHistoryUiState.update { it.copy(petWalkLoading = true) }
                when (val result = bookingHistoryRepository.getPetWalkBookings()) {
                    is Result.Success -> {
                        _bookingHistoryUiState.update {
                            it.copy(petWalkBookings = result.data, petWalkError = null)
                        }
                    }

                    is Result.Error -> {
                        _bookingHistoryUiState.update { it.copy(petWalkError = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("PetWalkBookings", e.toString())
            } finally {
                _bookingHistoryUiState.update { it.copy(petWalkLoading = false, petWalkLoaded = true) }
            }
        }
    }

    fun getVetBookings() {
        viewModelScope.launch {
            try {
                _bookingHistoryUiState.update { it.copy(vetLoading = true) }
                when (val result = bookingHistoryRepository.getVetBookings()) {
                    is Result.Success -> {
                        _bookingHistoryUiState.update {
                            it.copy(vetBookings = result.data, vetError = null)
                        }
                    }

                    is Result.Error -> {
                        _bookingHistoryUiState.update { it.copy(vetError = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("VetBookings", e.toString())
            } finally {
                _bookingHistoryUiState.update { it.copy(vetLoading = false, vetLoaded = true) }
            }
        }
    }

    private fun observeBookingEvents() {
        viewModelScope.launch {
            bookingEventManager.events.collect { event ->
                if (event is BookingEvent.BookingPlaced) {
                    when (event.category) {
                        Category.GROOMING -> {
                            getGroomingBookings()
                        }

                        Category.DOG_TRAINING -> {
                            getDogTrainingBookings()
                        }

                        Category.WALKING -> {
                            getPetWalkBookings()
                        }

                        Category.VETERINARY -> {
                            getVetBookings()
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
