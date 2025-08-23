package com.example.theworldofpuppies.profile.pet.domain

import android.net.Uri
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender

data class PetEditUiState(
    val petPictureUri: Uri? = null,
    val name: String? = null,
    val age: String? = null,
    val weight: String? = null,
    val breed: DogBreed? = null,
    val gender: Gender? = null,
    val aggression: Aggression? = null,
    val isVaccinated: Boolean? = null,
)
