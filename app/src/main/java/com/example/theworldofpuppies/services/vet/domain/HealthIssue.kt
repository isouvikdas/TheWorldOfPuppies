package com.example.theworldofpuppies.services.vet.domain

import kotlinx.serialization.Serializable

@Serializable
data class HealthIssue(
    val name: String,
    val description: String? = null
)
