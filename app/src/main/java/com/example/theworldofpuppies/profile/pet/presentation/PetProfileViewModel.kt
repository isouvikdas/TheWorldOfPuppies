package com.example.theworldofpuppies.profile.pet.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.core.presentation.SearchUiState
import com.example.theworldofpuppies.profile.pet.domain.PetUiState
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender
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

class PetProfileViewModel() : ViewModel() {
    private val _petUiState = MutableStateFlow(PetUiState())
    val petUiState: StateFlow<PetUiState> = _petUiState.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _changedBreed = MutableStateFlow<DogBreed?>(null)
    val changedBreed: StateFlow<DogBreed?> = _changedBreed.asStateFlow()

    private val _changeVaccinated = MutableStateFlow(false)
    val changeVaccinated: StateFlow<Boolean> = _changeVaccinated.asStateFlow()

    private val _changeAggression = MutableStateFlow<Aggression?>(null)
    val changeAggression: StateFlow<Aggression?> = _changeAggression.asStateFlow()

    private val _changeGender = MutableStateFlow<Gender?>(null)
    val changeGender: StateFlow<Gender?> = _changeGender.asStateFlow()

    private val _changeWeight = MutableStateFlow("")
    val changeWeight: StateFlow<String> = _changeWeight.asStateFlow()

    private val _changeAge = MutableStateFlow("")
    val changeAge: StateFlow<String> = _changeAge.asStateFlow()

    private val _changeName = MutableStateFlow("")
    val changeName: StateFlow<String> = _changeName.asStateFlow()

    private val _showModalBottomSheet = mutableStateOf(false)
    val showModalSheet = _showModalBottomSheet

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _breedList = MutableStateFlow(DogBreed.entries)

    fun onNameChange(name: String) {
        _changeName.value = name
    }

    fun onAgeChange(age: String) {
        _changeAge.value = age
    }

    fun onWeightChange(weight: String) {
        _changeWeight.value = weight
    }

    fun onAggressionChange(aggression: Aggression) {
        _changeAggression.value = aggression
    }

    fun onGenderChange(gender: Gender) {
        _changeGender.value = gender
    }

    fun onVaccinatedChange(vaccinated: Boolean) {
        _changeVaccinated.value = vaccinated
    }

    fun selectBreed(breed: DogBreed) {
        _changedBreed.value = breed
        toggleModalBottomSheet()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun toggleModalBottomSheet() {
        _showModalBottomSheet.value = !_showModalBottomSheet.value
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
                        _breedList.value.filter {
                            it.doesMatchSearchQuery(query)
                        }
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

    fun validateFields(): Boolean {
        var isValid = true

        val breedError = if (petUiState.value.breedError.isNullOrBlank()) {
            isValid = false
            "Please select a breed"
        } else {
            null
        }
        val nameError = if (petUiState.value.nameError.isNullOrBlank()) {
            isValid = false
            "Name cannot be empty"
        } else {
            null
        }
        val ageError = if (petUiState.value.ageError.isNullOrBlank()) {
            isValid = false
            "Age cannot be empty"
        } else {
            null
        }
        val genderError = if (petUiState.value.genderError.isNullOrBlank()) {
            isValid = false
            "Please select the gender"
        } else {
            null
        }
        val aggressionError = if (petUiState.value.aggressionError.isNullOrBlank()) {
            isValid = false
            "Please select the aggression level"
        } else {
            null
        }
        val petPictureError = if (petUiState.value.petPictureError.isNullOrBlank()) {
            isValid = false
            "Please put a profile picture"
        } else {
            null
        }
        val weightError = if (petUiState.value.weightError.isNullOrBlank()) {
            isValid = false
            "Weight cannot be empty"
        } else {
            null
        }
        _petUiState.update {
            it.copy(
                breedError = breedError,
                nameError = nameError,
                ageError = ageError,
                genderError = genderError,
                aggressionError = aggressionError,
                petPictureError = petPictureError,
                weightError = weightError
            )
        }
        return isValid

    }
}