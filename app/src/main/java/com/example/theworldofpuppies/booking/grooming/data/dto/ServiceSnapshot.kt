package com.example.theworldofpuppies.booking.grooming.data.dto

import com.example.theworldofpuppies.services.domain.enums.ServiceCategory
import com.example.theworldofpuppies.services.grooming.domain.SubService
import kotlinx.serialization.Serializable

@Serializable
data class ServiceSnapshot(
    val name: String,
    val category: ServiceCategory,
    val petSubService: SubService? = null,
    val description: String
)
