package com.example.theworldofpuppies.profile.user.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.Event
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.profile.user.domain.UpdateUserRepository
import com.example.theworldofpuppies.profile.user.domain.UpdateUserUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdateUserViewModel(
    private val userRepository: UserRepository,
    private val updateUserRepository: UpdateUserRepository,
    private val authEventManager: AuthEventManager
) : ViewModel() {

    private val _updateUserUiState = MutableStateFlow(UpdateUserUiState())
    val updateUserUiState = _updateUserUiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    suspend fun showToastMessage(message: String) {
        _toastEvent.emit(message)
    }

    init {
        observeAuthEvents()
        if (!userRepository.getUserId().isNullOrEmpty()) {
            loadUserProfile()
        }
    }

    fun onPictureChange(uri: Uri) {
        _updateUserUiState.update { it.copy(imageUri = uri) }
    }

    fun onNameChange(name: String) {
        _updateUserUiState.update { it.copy(username = name) }
    }

    fun onEmailChange(email: String) {
        _updateUserUiState.update { it.copy(email = email) }
    }

    fun saveProfile(context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                val state = _updateUserUiState.value
                if (!validateFields()) {
                    return@launch
                }
                if (state.username == userRepository.getUserName()
                    && state.email == userRepository.getUserEmail()
                    && state.imageUri.toString() == userRepository.getFetchUrl()) {
                    showToastMessage("No changes to save")
                    return@launch
                }
                _updateUserUiState.update { it.copy(isLoading = true) }
                when (val result = updateUserRepository.updateUser(
                    imageUri = state.imageUri,
                    username = state.username,
                    email = state.email
                )) {
                    is Result.Success -> {
                        userRepository.saveUserData(
                            username = result.data.username,
                            email = result.data.email,
                            fetchUrl = result.data.fetchUrl
                        )
                        navController.popBackStack()
                        loadUserProfile()
                    }

                    is Result.Error -> {
                        showToastMessage(result.error.toString(context))
                    }
                }

            } catch (e: Exception) {
                Log.i("pet", e.message.toString())
            } finally {
                _updateUserUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loadUserProfile(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (forceRefresh) {
                _updateUserUiState.update { it.copy(isRefreshing = true) }
            }
            val imageUri = userRepository.getFetchUrl()
            val username = userRepository.getUserName()
            val email = userRepository.getUserEmail()
            val phoneNumber = userRepository.getUserPhoneNumber()
            imageUri?.let {
                onPictureChange(imageUri.toUri())
            }
            username?.let {
                onNameChange(username)
            }
            email?.let {
                onEmailChange(email)
            }
            phoneNumber?.let {
                _updateUserUiState.update { it.copy(phoneNumber = phoneNumber) }
            }
            if (forceRefresh) {
                delay(300)
                _updateUserUiState.update { it.copy(isRefreshing = false) }
            }
        }
    }


    fun validateFields(): Boolean {
        var isValid = true
        val edits = _updateUserUiState.value

        val nameError = if (edits.username.isBlank()) {
            isValid = false; "Name cannot be empty"
        } else null

        val emailError = if (!validateEmail(edits.email)) {
            isValid = false; "Email is not valid"
        } else null

        _updateUserUiState.update {
            it.copy(
                usernameError = nameError,
                emailError = emailError
            )
        }
        return isValid
    }

    private fun validateEmail(email: String): Boolean {
        val gmailPattern = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com\$")
        return gmailPattern.matches(email)
    }

    fun observeAuthEvents() {
        viewModelScope.launch {
            authEventManager.events.collect { event ->
                if (event is Event.LoggedIn) {
                    loadUserProfile()
                }
            }
        }
    }

}