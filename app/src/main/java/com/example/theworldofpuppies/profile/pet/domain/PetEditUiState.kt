package com.example.theworldofpuppies.profile.pet.domain

import android.net.Uri
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender

data class PetEditUiState(
    val petPictureUri: Uri = Uri.EMPTY,
    val name: String = "",
    val age: String = "",
    val weight: String = "",
    val breed: DogBreed = DogBreed.GERMAN_SHEPHERD,
    val gender: Gender = Gender.NULL,
    val aggression: Aggression = Aggression.NULL,
    val isVaccinated: Boolean = false,

    val isLoading: Boolean = false,

    // Validation error states
    val petPictureError: String? = null,
    val nameError: String? = null,
    val breedError: String? = null,
    val ageError: String? = null,
    val genderError: String? = null,
    val aggressionError: String? = null,
    val weightError: String? = null,

)
