package com.example.theworldofpuppies.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.auth.data.request.LoginRequest
import com.example.theworldofpuppies.auth.data.request.OtpRequest
import com.example.theworldofpuppies.auth.domain.AuthApi
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.onError
import com.example.theworldofpuppies.core.domain.util.onSuccess
import com.example.theworldofpuppies.core.presentation.util.Event
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val phoneNumber: String = "",
    val otp: String = "",
    val isOtpSent: Boolean = false,
    val loginSuccess: Boolean = false,
    val isOtpVerified: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val userRepository: UserRepository,
    private val authApi: AuthApi,
    private val authEventManager: AuthEventManager
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    init {
        val token = userRepository.getToken()
        Log.i("token", token ?: "token not found")
    }

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    fun resetState() {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState()
        }
    }

    fun toggleOtpSentState() {
        viewModelScope.launch {
            _loginUiState.update { it.copy(isOtpSent = !_loginUiState.value.isOtpSent) }
            Log.i("toggle", "isOtpSent: ${_loginUiState.value.isOtpSent}")

        }
    }


    fun loginUser(phoneNumber: String) {
        viewModelScope.launch {
            _loginUiState.update { it.copy(isLoading = true) }
            Log.i("toggle", "started")
            val result = authApi.loginUser(LoginRequest(phoneNumber))
            Log.i("toggle", "result came: ${result.onSuccess { it.success.toString() }}")
            result.onSuccess { apiResponse ->
                _loginUiState.update {
                    if (apiResponse.success) {
                        it.copy(
                            phoneNumber = phoneNumber,
                            isOtpSent = true,
                            errorMessage = null,
                            isLoading = false
                        )
                    } else {
                        it.copy(
                            errorMessage = apiResponse.message,
                            isLoading = false
                        )
                    }
                }
            }.onError { error ->
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toString()
                    )
                }
                _events.send(Event.Error(error))
            }
        }
    }

    fun verifyLogin(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _loginUiState.update { it.copy(isLoading = true) }
            val result = authApi.verifyLogin(OtpRequest(phoneNumber, otp))
            result.onSuccess { apiResponse ->
                _loginUiState.update {
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
                            loginSuccess = true
                        )

                    } else {
                        it.copy(
                            errorMessage = apiResponse.message,
                            isLoading = false
                        )
                    }
                }
            }.onError { error ->
                _loginUiState.update {
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