package com.example.theworldofpuppies.review.data.dto

import com.example.theworldofpuppies.review.domain.TargetType
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val id: String,
    val targetId: String,
    val productId: String = "",
    val targetType: TargetType,
    val stars: Int,
    val description: String
)