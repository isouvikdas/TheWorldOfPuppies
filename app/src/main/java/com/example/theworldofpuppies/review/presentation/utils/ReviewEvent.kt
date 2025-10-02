package com.example.theworldofpuppies.review.presentation.utils

import com.example.theworldofpuppies.core.domain.util.NetworkError

sealed class ReviewEvent {
    data class ReviewFailed(val error: NetworkError): ReviewEvent()
    data class ReviewConfirmed(val targetId: String) : ReviewEvent()
}