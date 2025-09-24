package com.example.theworldofpuppies.review.domain

data class Review(
    val id: String,
    val targetId: String,
    val productId: String,
    val targetType: TargetType,
    val stars: Int,
    val description: String
)
