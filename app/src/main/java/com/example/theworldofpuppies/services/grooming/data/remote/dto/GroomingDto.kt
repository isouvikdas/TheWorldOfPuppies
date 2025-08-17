package com.example.theworldofpuppies.services.grooming.data.remote.dto

import com.example.theworldofpuppies.services.domain.enums.ServiceCategory
import com.example.theworldofpuppies.services.grooming.domain.SubService
import kotlinx.serialization.Serializable

@Serializable
data class GroomingDto(
    val id: String,
    val name: String,
    val description: String,
    val discount: Int,
    val category: ServiceCategory,
    val subServiceIds: List<String>,
    val petSubServices: List<SubService>,
    val basePrice: Double,
    val active: Boolean
)