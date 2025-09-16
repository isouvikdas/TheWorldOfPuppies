package com.example.theworldofpuppies.pet_insurance.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.core.presentation.SearchUiState
import com.example.theworldofpuppies.pet_insurance.domain.PetInsuranceUiState
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PetInsuranceViewModel(): ViewModel() {

    private val _petInsuranceUiState = MutableStateFlow(PetInsuranceUiState())
    val petInsuranceUiState = _petInsuranceUiState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _breedList = MutableStateFlow(DogBreed.entries)

    fun selectBreed(breed: DogBreed) {
        _petInsuranceUiState.update { it.copy(breed = breed) }
        toggleModalBottomSheet()
    }

    fun onNameChange(name: String) {
        _petInsuranceUiState.update { it.copy(name = name) }
    }

    fun onAgeChange(age: String) {
        _petInsuranceUiState.update { it.copy(age = age) }
    }
    fun onEmailChange(email: String) {
        _petInsuranceUiState.update { it.copy(email = email) }
    }
    fun onContactNumberChange(contactNumber: String) {
        _petInsuranceUiState.update { it.copy(contactNumber = contactNumber) }
    }

    // ---------- search ----------
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchedBreedFlow = searchText
        .flatMapLatest { query ->
            flow {
                try {
                    _isSearching.emit(true)
                    val filtered = if (query.isBlank()) {
                        _breedList.value
                    } else {
                        delay(300L)
                        _breedList.value.filter { it.doesMatchSearchQuery(query) }
                    }
                    emit(filtered)
                } finally {
                    _isSearching.emit(false)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _breedList.value
        )

    val searchUiState = combine(
        searchText,
        searchedBreedFlow,
        isSearching
    ) { query, results, loading ->
        SearchUiState(
            query = query,
            results = results,
            isSearching = loading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SearchUiState()
    )

    fun onBookNowClick(navController: NavController) {

    }

    fun toggleModalBottomSheet() {
        viewModelScope.launch {
            _petInsuranceUiState.update {
                it.copy(
                    showModalBottomSheet = !it.showModalBottomSheet
                )
            }
        }
    }

    fun validateFields(): Boolean {
        var isValid = true
        val uiState = petInsuranceUiState.value

        val breedError = if (uiState.breed == null) {
            isValid = false; "Please select a breed"
        } else null

        val nameError = if (uiState.name.isNullOrEmpty()) {
            isValid = false; "Name cannot be empty"
        } else null

        val ageError = if (uiState.age.isNullOrEmpty()) {
            isValid = false; "Age cannot be empty"
        } else null

        val contactNumberError = if (uiState.contactNumber.isNullOrEmpty()) {
            isValid = false; "Please enter a contact number"
        } else null

        val emailError = if (uiState.email.isNullOrEmpty()) {
            isValid = false; "Please enter a email"
        } else null

        _petInsuranceUiState.update {
            it.copy(
                breedError = breedError,
                nameError = nameError,
                ageError = ageError,
                emailError = emailError,
                contactNumberError = contactNumberError,
            )
        }
        return isValid
    }

}