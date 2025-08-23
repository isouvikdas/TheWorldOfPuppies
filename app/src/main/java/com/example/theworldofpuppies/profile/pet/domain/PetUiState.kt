package com.example.theworldofpuppies.profile.pet.domain

import android.net.Uri
import com.example.theworldofpuppies.core.domain.util.Error
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender

data class PetUiState(
    val petPicture: Uri = Uri.EMPTY,
    val name: String = "",
    val breed: DogBreed? = null,
    val age: String ="",
    val weight: String = "",
    val gender: Gender? = null,
    val aggression: Aggression? = null,
    val isVaccinated: Boolean = false,
    val isLoading: Boolean = false,
    val error: Error? = null,

    // Validation error states
    val petPictureError: String? = null,
    val nameError: String? = null,
    val breedError: String? = null,
    val ageError: String? = null,
    val genderError: String? = null,
    val aggressionError: String? = null,
    val weightError: String? = null,
)