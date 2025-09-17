package com.example.theworldofpuppies.pet_insurance.domain

import com.example.theworldofpuppies.booking.core.domain.Category

data class PetInsurance(
    val id: String,
    val name: String,
    val category: Category,
    val description: String,
    val active: Boolean
)
