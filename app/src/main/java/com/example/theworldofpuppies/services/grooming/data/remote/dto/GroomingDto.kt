package com.example.theworldofpuppies.services.grooming.data.remote.dto

import com.example.theworldofpuppies.services.core.domain.ServiceCategory
import com.example.theworldofpuppies.services.grooming.domain.GroomingSubService
import kotlinx.serialization.Serializable

@Serializable
data class GroomingDto(
    val id: String,
    val name: String,
    val description: String,
    val discount: Int,
    val category: ServiceCategory,
    val subServiceIds: List<String>,
    val groomingSubServices: List<GroomingSubService>,
    val basePrice: Double,
    val active: Boolean
)