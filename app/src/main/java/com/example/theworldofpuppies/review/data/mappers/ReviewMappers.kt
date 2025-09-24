package com.example.theworldofpuppies.review.data.mappers

import com.example.theworldofpuppies.review.data.dto.ReviewDto
import com.example.theworldofpuppies.review.domain.Review

fun ReviewDto.toReview(): Review {
    return Review(
        id = id,
        targetId = targetId,
        productId = productId,
        targetType = targetType,
        stars = stars,
        description = description
    )
}