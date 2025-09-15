package com.example.theworldofpuppies.services.grooming.domain

import com.example.theworldofpuppies.services.core.domain.ServiceCategory

data class Grooming(
    val id: String,
    val name: String,
    val description: String,
    val discount: Int,
    val category: ServiceCategory,
    val subServiceIds: List<String>,
    val subServices: List<GroomingSubService>,
    val basePrice: Double,
    val active: Boolean
)
