package com.example.theworldofpuppies.booking.grooming.data.dto

import com.example.theworldofpuppies.services.core.domain.ServiceCategory
import com.example.theworldofpuppies.services.grooming.domain.GroomingSubService
import kotlinx.serialization.Serializable

@Serializable
data class GroomingSnapshot(
    val name: String,
    val category: ServiceCategory,
    val groomingSubService: GroomingSubService? = null,
    val description: String
)
