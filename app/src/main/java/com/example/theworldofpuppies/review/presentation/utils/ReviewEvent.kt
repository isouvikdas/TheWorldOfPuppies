package com.example.theworldofpuppies.review.presentation.utils

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.review.domain.TargetType

sealed class ReviewEvent {
    data class ReviewFailed(val error: NetworkError) : ReviewEvent()
    data class ReviewConfirmed(val targetId: String, val targetType: TargetType) : ReviewEvent()
}