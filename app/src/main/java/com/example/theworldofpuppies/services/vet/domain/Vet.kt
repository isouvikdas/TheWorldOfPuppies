package com.example.theworldofpuppies.services.vet.domain

import com.example.theworldofpuppies.booking.core.domain.Category

data class Vet(
    val id: String,
    val name: String,
    val discount: Int,
    val category: Category,
    val description: String,
    val active: Boolean,
    val vetOptions: List<VetOption>,
    val vetTimeSlots: List<VetTimeSlot>,
    val healthIssues: List<HealthIssue>,
    val isRated: Boolean = false,
    val averageStars: Double = 0.0,
    val totalReviews: Int = 0
)
