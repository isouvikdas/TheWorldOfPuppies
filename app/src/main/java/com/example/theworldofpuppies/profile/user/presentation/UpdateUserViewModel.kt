package com.example.theworldofpuppies.profile.user.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.profile.user.domain.UpdateUserRepository
import com.example.theworldofpuppies.profile.user.domain.UpdateUserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UpdateUserViewModel(
    private val userRepository: UserRepository,
    private val updateUserRepository: UpdateUserRepository
) : ViewModel() {

    private val _updateUserUiState = MutableStateFlow(UpdateUserUiState())
    val updateUserUiState = _updateUserUiState.asStateFlow()

    fun onPictureChange(uri: Uri) {
        _updateUserUiState.update { it.copy(imageUri = uri) }
    }

    fun onNameChange(name: String) {
        _updateUserUiState.update { it.copy(username = name) }
    }

    fun onEmailChange(email: String) {
        _updateUserUiState.update { it.copy(email = email) }
    }



}