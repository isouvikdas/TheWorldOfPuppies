package com.example.theworldofpuppies.profile.pet.domain

import kotlinx.serialization.Serializable

@Serializable
data class PetImage(
    val fileName: String,
    val fileType: String
)