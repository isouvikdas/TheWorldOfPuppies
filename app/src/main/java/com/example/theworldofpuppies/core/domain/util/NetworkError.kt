package com.example.theworldofpuppies.core.domain.util

enum class NetworkError : Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN,
    UNAUTHORIZED,
    PAYMENT_VERIFICATION_FAILED,
    INVALID_PHONE_NUMBER,
    SERVICE_NOT_AVAILABLE,
}