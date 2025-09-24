package com.example.theworldofpuppies.review.data.request

import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.review.domain.TargetType
import kotlinx.serialization.Serializable

@Serializable
data class AddReviewRequest(
    val targetType: TargetType,
    val subType: Category? = null,
    val targetId: String,
    val stars: Int,
    val description: String
)
