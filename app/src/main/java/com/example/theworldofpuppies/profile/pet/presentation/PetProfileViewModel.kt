package com.example.theworldofpuppies.profile.pet.presentation

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.core.presentation.SearchUiState
import com.example.theworldofpuppies.profile.pet.domain.PetEditUiState
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
import kotlinx.coroutines.launch

class PetProfileViewModel() : ViewModel() {

    // Original fetched data
    private val _petUiState = MutableStateFlow(PetUiState())
    val petUiState: StateFlow<PetUiState> = _petUiState.asStateFlow()

    // User edits (all fields grouped in one state)
    private val _editState = MutableStateFlow(PetEditUiState())
    val editState: StateFlow<PetEditUiState> = _editState.asStateFlow()

    // UI states
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _showModalBottomSheet = mutableStateOf(false)
    val showModalSheet = _showModalBottomSheet

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _breedList = MutableStateFlow(DogBreed.entries)

    // ---------- field change handlers ----------

    fun onPetPictureChange(uri: Uri?) {
        _editState.update { it.copy(petPictureUri = uri) }
    }

    fun onNameChange(name: String) {
        _editState.update { it.copy(name = name) }
    }

    fun onAgeChange(age: String) {
        _editState.update { it.copy(age = age) }
    }

    fun onWeightChange(weight: String) {
        _editState.update { it.copy(weight = weight) }
    }

    fun onAggressionChange(aggression: Aggression) {
        _editState.update { it.copy(aggression = aggression) }
    }

    fun onGenderChange(gender: Gender) {
        _editState.update { it.copy(gender = gender) }
    }

    fun onVaccinatedChange(vaccinated: Boolean) {
        _editState.update { it.copy(isVaccinated = vaccinated) }
    }

    fun selectBreed(breed: DogBreed) {
        _editState.update { it.copy(breed = breed) }
        toggleModalBottomSheet()
    }

    // ---------- search ----------
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

    // ---------- validation ----------
    fun validateFields(): Boolean {
        var isValid = true
        val current = petUiState.value
        val edits = editState.value

        val breedError = if ((edits.breed ?: current.breed) == null) {
            isValid = false; "Please select a breed"
        } else null

        val nameError = if ((edits.name ?: current.name).isBlank()) {
            isValid = false; "Name cannot be empty"
        } else null

        val ageError = if ((edits.age ?: current.age).isBlank()) {
            isValid = false; "Age cannot be empty"
        } else null

        val genderError = if ((edits.gender ?: current.gender) == null) {
            isValid = false; "Please select the gender"
        } else null

        val aggressionError = if ((edits.aggression ?: current.aggression) == null) {
            isValid = false; "Please select the aggression level"
        } else null

        val petPictureError = if (current.petPicture == Uri.EMPTY) {
            isValid = false; "Please put a profile picture"
        } else null

        val weightError = if ((edits.weight ?: current.weight).isBlank()) {
            isValid = false; "Weight cannot be empty"
        } else null

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

    // ---------- save profile ----------
    fun saveProfile() {
        viewModelScope.launch {
            if (validateFields()) {
                val current = _petUiState.value
                val edits = _editState.value
                val updatedPet = current.copy(
                    name = edits.name ?: current.name,
                    age = edits.age ?: current.age,
                    weight = edits.weight ?: current.weight,
                    breed = edits.breed ?: current.breed,
                    gender = edits.gender ?: current.gender,
                    aggression = edits.aggression ?: current.aggression,
                    isVaccinated = edits.isVaccinated ?: current.isVaccinated,
                )
                _petUiState.value = updatedPet
                _editState.value = PetEditUiState() // reset edits

            } else {
                return@launch
            }

        }


    }
}
