package com.example.theworldofpuppies.review.domain

import com.example.theworldofpuppies.booking.core.domain.Category
import java.time.LocalDateTime

data class Review(
    val id: String,
    val userId: String,
    val userName: String,
    val targetId: String,
    val productId: String? = null,
    val targetType: TargetType,
    val stars: Int,
    val description: String,
    val subType: Category? = null,
    val createdAt: LocalDateTime?
)
