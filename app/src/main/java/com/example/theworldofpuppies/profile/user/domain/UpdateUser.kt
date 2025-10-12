package com.example.theworldofpuppies.profile.user.domain

import com.example.theworldofpuppies.profile.pet.domain.PetImage

data class UpdateUser(
    val email: String?,
    val username: String?,
    val image: PetImage?,
    val fetchUrl: String?
)
