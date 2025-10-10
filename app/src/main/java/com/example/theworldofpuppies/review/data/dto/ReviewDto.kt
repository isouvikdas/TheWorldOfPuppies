package com.example.theworldofpuppies.review.data.dto

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.review.domain.TargetType
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val id: String,
    val targetId: String,
    val userId: String,
    val userName: String,
    val productId: String? = null,
    val targetType: TargetType,
    val stars: Int,
    val description: String,
    val subType: Category? = null,
    val createdAt: Long? = null
)