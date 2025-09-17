package com.example.theworldofpuppies.pet_insurance.domain

import kotlinx.serialization.Serializable

@Serializable
data class PetInsuranceSnapshot(
    val name: String,
    val description: String
)