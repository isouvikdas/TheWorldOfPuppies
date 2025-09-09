package com.example.theworldofpuppies.booking.grooming.data.dto

import com.example.theworldofpuppies.services.domain.enums.ServiceCategory
import com.example.theworldofpuppies.services.grooming.domain.GroomingSubService
import kotlinx.serialization.Serializable

@Serializable
data class GroomingSnapshot(
    val name: String,
    val category: ServiceCategory,
    val petSubService: GroomingSubService? = null,
    val description: String
)
