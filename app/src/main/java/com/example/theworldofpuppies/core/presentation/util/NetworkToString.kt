package com.example.theworldofpuppies.core.presentation.util

import android.content.Context
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.domain.util.NetworkError

fun NetworkError.toString(context: Context): String {
    val resId = when (this) {
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.SERVER_ERROR -> R.string.error_unknown
        NetworkError.SERIALIZATION -> R.string.error_serialization
        NetworkError.UNKNOWN -> R.string.error_unknown
        NetworkError.UNAUTHORIZED -> R.string.error_unauthorized
        NetworkError.PAYMENT_VERIFICATION_FAILED -> R.string.payment_verification_failed
        NetworkError.INVALID_PHONE_NUMBER -> R.string.invalid_phone_number
        NetworkError.SERVICE_NOT_AVAILABLE -> R.string.service_not_available
    }
    return context.getString(resId)
}