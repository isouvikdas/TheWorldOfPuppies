package com.example.theworldofpuppies.review.domain

import com.example.theworldofpuppies.booking.core.domain.Category

data class Review(
    val id: String,
    val targetId: String,
    val productId: String,
    val targetType: TargetType,
    val stars: Int,
    val description: String,
    val subType: Category? = null
)
