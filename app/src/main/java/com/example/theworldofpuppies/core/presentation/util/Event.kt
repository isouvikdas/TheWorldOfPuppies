package com.example.theworldofpuppies.core.presentation.util

import com.example.theworldofpuppies.core.domain.util.NetworkError

sealed interface Event {
    data class Error(val error: NetworkError): Event
    object LoggedIn : Event
}