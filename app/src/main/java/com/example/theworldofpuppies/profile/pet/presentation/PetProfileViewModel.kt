package com.example.theworldofpuppies.profile.pet.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.SearchUiState
import com.example.theworldofpuppies.core.presentation.util.Event
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.pet.domain.Pet
import com.example.theworldofpuppies.profile.pet.domain.PetEditUiState
import com.example.theworldofpuppies.profile.pet.domain.PetListUiState
import com.example.theworldofpuppies.profile.pet.domain.PetRepository
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PetProfileViewModel(
    private val petRepository: PetRepository,
    private val userRepository: UserRepository,
    private val authEventManager: AuthEventManager
) : ViewModel() {

    private val _petListUiState = MutableStateFlow(PetListUiState())
    val petListUiState = _petListUiState.asStateFlow()

    private val _selectedPet = MutableStateFlow<Pet?>(null)

    private val _isEditing = MutableStateFlow(false)

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    // Original fetched data
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // User edits (all fields grouped in one state)
    private val _editState = MutableStateFlow(PetEditUiState())
    val editState: StateFlow<PetEditUiState> = _editState.asStateFlow()

    private val _showModalBottomSheet = mutableStateOf(false)
    val showModalSheet = _showModalBottomSheet

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _breedList = MutableStateFlow(DogBreed.entries)

    private val _isPetSelectionView = MutableStateFlow(false)
    val isPetSelectionView = _isPetSelectionView.asStateFlow()

    private val selectedServiceCategory = MutableStateFlow<Category?>(null)

    private val _selectedPetForService = MutableStateFlow<Pet?>(null)
    val selectedPetForService = _selectedPetForService.asStateFlow()
    // ---------- field change handlers ----------

    init {
        if (!userRepository.getUserId().isNullOrEmpty()) {
            getPets()
        }
        observeAuthEvents()
    }

    fun onBookNowClick(navController: NavController) {
        viewModelScope.launch {
            if (selectedPetForService.value == null) {
                showToastMessage("Please select a pet first")
                return@launch
            }
            when (selectedServiceCategory.value) {
                Category.DOG_TRAINING -> {
                    navController.navigate(Screen.DogTrainingBookingScreen.route)
                }

                Category.WALKING -> {
                    navController.navigate(Screen.BookingPetWalkScreen.route)
                }

                Category.VETERINARY -> {
                    navController.navigate(Screen.VetIssuesScreen.route)
                }

                Category.GROOMING -> {
                    navController.navigate(Screen.BookingGroomingScreen.route)
                }

                else -> {

                }
            }
        }
    }

    fun changePetSelectionView(isPetSelectionView: Boolean, selectedService: Category? = null) {
        _isPetSelectionView.value = isPetSelectionView
        selectedServiceCategory.value = selectedService
    }

    fun onPetSelection(pet: Pet) {
        _selectedPetForService.value = pet
    }

    fun changeEditingState(isEditing: Boolean, pet: Pet? = null) {
        _isEditing.value = isEditing
        if (isEditing) {
            _selectedPet.value = pet
        } else {
            resetPetUiState()
            _selectedPet.value = null
        }

    }

    fun fillExistingPetData(pet: Pet) {
        viewModelScope.launch {
            resetPetUiState()
            changeEditingState(true, pet)
            onPetPictureChange(pet.downloadUrl.toUri())
            onNameChange(pet.name)
            onAgeChange(pet.age)
            onWeightChange(pet.weight)
            selectBreed(pet.breed)
            onGenderChange(pet.gender)
            onAggressionChange(pet.aggression)
            onVaccinatedChange(pet.isVaccinated)
        }
    }

    fun onPetPictureChange(uri: Uri) {
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
    }

    // ---------- search ----------
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun toggleModalBottomSheet() {
        _showModalBottomSheet.value = !_showModalBottomSheet.value
    }

    suspend fun showToastMessage(message: String) {
        _toastEvent.emit(message)
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
        val edits = editState.value

        val nameError = if (edits.name.isBlank()) {
            isValid = false; "Name cannot be empty"
        } else null

        val ageError = if (edits.age.isBlank()) {
            isValid = false; "Age cannot be empty"
        } else null

        val genderError = if (edits.gender == Gender.NULL) {
            isValid = false; "Please select the gender"
        } else null

        val aggressionError = if (edits.aggression == Aggression.NULL) {
            isValid = false; "Please select the aggression level"
        } else null

        val petPictureError = if (edits.petPictureUri == Uri.EMPTY) {
            isValid = false; "Please put a profile picture"
        } else null

        val weightError = if (edits.weight.isBlank()) {
            isValid = false; "Weight cannot be empty"
        } else null

        _editState.update {
            it.copy(
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

    fun loadPetProfile(forceRefresh: Boolean = false, selectedPet: Pet? = null) {
        val state = editState.value
        if (forceRefresh || (!state.isLoading && state.name.isEmpty())) {
            if (selectedPet != null) {
                fillExistingPetData(selectedPet)
            } else {
                resetPetUiState()
            }
        }
    }

    fun resetPetUiState() {
        _editState.value = PetEditUiState()
    }

    fun getPets() {
        viewModelScope.launch {
            val petIds = userRepository.getPetIds()  // <-- call only once
            Log.d("getPets", "PetIds from prefs: $petIds")

            if (petIds.isEmpty()) {
                Log.d("getPets", "Returning early, no API call")
                _petListUiState.update { it.copy(pets = emptyList()) }
                return@launch
            }

            try {
                _isLoading.value = true

                when (val result = petRepository.getPets(petIds)) {
                    is Result.Success -> {
                        _petListUiState.update { it.copy(pets = result.data) }
                    }

                    is Result.Error -> {
                        showToastMessage(result.error.toString())
                    }
                }
            } catch (e: Exception) {
                Log.e("getPets", "Error fetching pets", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveProfile(context: Context, navController: NavController) {
        viewModelScope.launch {
            if (_isEditing.value && _selectedPet.value != null) {
                val selectedPet = _selectedPet.value!!
                val editState = editState.value
                if (selectedPet.name == editState.name
                    && selectedPet.breed == editState.breed
                    && selectedPet.age == editState.age
                    && selectedPet.gender == editState.gender
                    && selectedPet.aggression == editState.aggression
                    && selectedPet.isVaccinated == editState.isVaccinated
                    && selectedPet.weight == editState.weight
                    && selectedPet.downloadUrl == editState.petPictureUri.toString()
                ) {
                    showToastMessage("No changes to save")
                    return@launch
                } else {
                    updatePetProfile(context, navController)
                }
            } else {
                addNewProfile(context, navController)
            }

        }
    }

    // ---------- save profile ----------
    fun addNewProfile(context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                if (!validateFields()) {
                    return@launch
                } else {
                    when (val result = petRepository.addPet(
                        imageUri = editState.value.petPictureUri,
                        name = editState.value.name,
                        breed = editState.value.breed,
                        age = editState.value.age,
                        gender = editState.value.gender,
                        aggression = editState.value.aggression,
                        isVaccinated = editState.value.isVaccinated,
                        weight = editState.value.weight
                    )) {
                        is Result.Success -> {
                            _petListUiState.update {
                                it.copy(
                                    pets = it.pets + result.data
                                )
                            }
                            userRepository.savePetId(result.data.id)
                            navController.popBackStack()
                        }

                        is Result.Error -> {
                            showToastMessage(result.error.toString(context))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.i("pet", e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePetProfile(context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                if (!validateFields()) {
                    return@launch
                } else {
                    val imageUri =
                        if (editState.value.petPictureUri.toString() == _selectedPet.value?.downloadUrl) {
                            null
                        } else {
                            editState.value.petPictureUri
                        }
                    when (val result = petRepository.updatePet(
                        id = _selectedPet.value!!.id,
                        imageUri = imageUri,
                        name = editState.value.name,
                        breed = editState.value.breed,
                        age = editState.value.age,
                        gender = editState.value.gender,
                        aggression = editState.value.aggression,
                        isVaccinated = editState.value.isVaccinated,
                        weight = editState.value.weight
                    )) {
                        is Result.Success -> {
                            _petListUiState.update {
                                val pets = it.pets.toMutableList()
                                pets.remove(_selectedPet.value)
                                pets.add(result.data)
                                it.copy(
                                    pets = pets
                                )
                            }
                            navController.popBackStack()
                        }

                        is Result.Error -> {
                            showToastMessage(result.error.toString(context))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.i("pet", e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePet(petId: String, context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                when (val result = petRepository.deletePet(petId)) {
                    is Result.Success -> {
                        val pets = petListUiState.value.pets.filter { it.id != petId }
                        _petListUiState.update { it.copy(pets = pets) }
                        userRepository.removePetId(petId)
                    }

                    is Result.Error -> {
                        showToastMessage(result.error.toString(context))
                    }
                }
            } catch (e: Exception) {
                Log.i("pet", e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun observeAuthEvents() {
        viewModelScope.launch {
            authEventManager.events.collect { event ->
                if (event is Event.LoggedIn) {
                    getPets()
                }
            }
        }
    }
}
