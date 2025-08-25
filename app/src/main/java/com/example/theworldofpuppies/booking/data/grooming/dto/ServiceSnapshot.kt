package com.example.theworldofpuppies.booking.data.grooming.dto

import com.example.theworldofpuppies.services.domain.enums.ServiceCategory
import com.example.theworldofpuppies.services.grooming.domain.SubService
import kotlinx.serialization.Serializable

@Serializable
data class ServiceSnapshot(
    val name: String,
    val category: ServiceCategory,
    val subService: SubService,
    val description: String
)
