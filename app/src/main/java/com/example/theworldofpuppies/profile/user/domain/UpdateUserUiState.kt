package com.example.theworldofpuppies.profile.user.domain

import android.net.Uri

data class UpdateUserUiState(
    val imageUri: Uri = Uri.EMPTY,
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",

    val isLoading: Boolean = false,

    val emailError: String? = null,
    val usernameError: String? = null
)