package com.example.theworldofpuppies.services.dog_training.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.review.presentation.utils.ReviewEvent
import com.example.theworldofpuppies.review.presentation.utils.ReviewEventManager
import com.example.theworldofpuppies.services.dog_training.domain.DogTraining
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingFeature
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingOption
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingRepository
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DogTrainingViewModel(
    private val repository: DogTrainingRepository
) : ViewModel() {

    private val _dogTrainingUiState = MutableStateFlow(DogTrainingUiState())
    val dogTrainingUiState = _dogTrainingUiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun onProceedClick(navController: NavController) {
        viewModelScope.launch {
            val selectedFeatures = dogTrainingUiState.value.selectedDogTrainingFeatures
            if (selectedFeatures.isEmpty()) {
                showToast("Please select at least one feature")
            } else {
                navController.navigate(Screen.PetListScreen.route)
            }
        }
    }

    fun onTrainingFeatureSelect(feature: DogTrainingFeature) {
        viewModelScope.launch {
            val selectedFeatures = dogTrainingUiState.value.selectedDogTrainingFeatures
            _dogTrainingUiState.update {
                it.copy(
                    selectedDogTrainingFeatures = if (selectedFeatures.contains(feature)) {
                        selectedFeatures - feature
                    } else {
                        selectedFeatures + feature
                    }
                )
            }
        }
    }

    fun onTrainingOptionSelect(option: DogTrainingOption) {
        viewModelScope.launch {
            val selectedOption = dogTrainingUiState.value.selectedDogTrainingOption
            _dogTrainingUiState.update {
                it.copy(
                    selectedDogTrainingOption = if (selectedOption == option) {
                        null
                    } else {
                        option
                    },
                    selectedDogTrainingFeatures = emptyList()
                )
            }
        }
    }

    fun getDogTraining(context: Context) {
        viewModelScope.launch {
            _dogTrainingUiState.update { it.copy(isLoading = true, error = null) }
            try {
                when (val result = repository.getDogTraining()) {
                    is Result.Success -> {
                        val dogTraining = result.data
                        _dogTrainingUiState.update {
                            it.copy(
                                id = dogTraining.id,
                                name = dogTraining.name,
                                description = dogTraining.description,
                                discount = dogTraining.discount,
                                dogTrainingOptions = dogTraining.dogTrainingOptions,
                            )
                        }
                    }

                    is Result.Error -> {
                        _dogTrainingUiState.update { it.copy(error = result.error.toString(context)) }
                    }
                }
            } catch (e: Exception) {
                _dogTrainingUiState.update { it.copy(error = e.message.toString()) }
            } finally {
                _dogTrainingUiState.update { it.copy(isLoading = false) }
            }
        }
    }

}