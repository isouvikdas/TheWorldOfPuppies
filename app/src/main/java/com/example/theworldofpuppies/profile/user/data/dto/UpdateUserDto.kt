package com.example.theworldofpuppies.profile.user.data.dto

import com.example.theworldofpuppies.profile.pet.domain.PetImage
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    val username: String? = null,
    val email: String? = null,
    val image: PetImage? = null,
    val fetchUrl: String? = null
)
