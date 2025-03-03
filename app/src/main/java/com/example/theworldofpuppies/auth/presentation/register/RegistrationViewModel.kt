package com.example.theworldofpuppies.auth.presentation.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.auth.data.request.OtpRequest
import com.example.theworldofpuppies.auth.data.request.RegistrationRequest
import com.example.theworldofpuppies.auth.domain.AuthApi
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.onError
import com.example.theworldofpuppies.core.domain.util.onSuccess
import com.example.theworldofpuppies.core.presentation.util.Event
import com.example.theworldofpuppies.core.presentation.util.toString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val phoneNumber: String = "",
    val otp: String = "",
    val isOtpSent: Boolean = false,
    val registrationSuccess: Boolean = false,
    val isOtpVerified: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RegistrationViewModel(
    private val authEventManager: AuthEventManager,
    private val userRepository: UserRepository,
    private val authApi: AuthApi
) : ViewModel() {

    private val _registrationUiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState = _registrationUiState.asStateFlow()

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    fun registerUser(username: String, email: String, phoneNumber: String) {
        viewModelScope.launch {
            _registrationUiState.update { it.copy(isLoading = true) }
            val result = authApi.registerUser(RegistrationRequest(phoneNumber, email, username))
            result.onSuccess { apiResponse ->
                _registrationUiState.update {
                    if (apiResponse.success) {
                        it.copy(
                            phoneNumber = phoneNumber,
                            isOtpSent = true,
                            errorMessage = null,
                            isLoading = true
                        )
                    } else {
                        it.copy(
                            errorMessage = apiResponse.message,
                            isLoading = false
                        )
                    }
                }
            }.onError { error ->
                _registrationUiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = error.toString()
                    )
                }
                _events.send(Event.Error(error))
            }
        }
    }


    fun verifyRegistration(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _registrationUiState.update { it.copy(isLoading = true) }
            val result = authApi.verifyRegistration(OtpRequest(phoneNumber, otp))
            result.onSuccess { apiResponse ->
                _registrationUiState.update {
                    if (apiResponse.success) {
                        apiResponse.data?.let { userResponse ->
                            val token = userResponse.token
                            val expirationTime = userResponse.expirationTime
                            val userId = userResponse.userId
                            val number = userResponse.phoneNumber
                            val username = userResponse.username
                            val email = userResponse.email
                            Log.i("toggle", "token: $token")
                            Log.i("toggle", "expirationTime: $expirationTime")
                            Log.i("toggle", "userId: $userId")
                            Log.i("toggle", "phoneNUmber: $number")
                            Log.i("toggle", "username: $username")
                            Log.i("toggle", "email: $email")
                            userRepository.saveUserData(
                                token = userResponse.token,
                                expirationTime = userResponse.expirationTime,
                                userId = userResponse.userId,
                                phoneNumber = phoneNumber,
                                username = userResponse.username,
                                email = userResponse.email
                            )
                        }
                        authEventManager.sendEvent(Event.LoggedIn)
                        it.copy(
                            errorMessage = null,
                            isOtpVerified = true,
                            isLoading = false,
                            registrationSuccess = true
                        )

                    } else {
                        it.copy(
                            errorMessage = apiResponse.message,
                            isLoading = false
                        )
                    }
                }
            }.onError { error ->
                _registrationUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toString()
                    )
                }
                _events.send(Event.Error(error))

            }
        }
    }


}