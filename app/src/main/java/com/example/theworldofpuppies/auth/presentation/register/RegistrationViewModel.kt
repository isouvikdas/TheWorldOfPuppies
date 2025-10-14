package com.example.theworldofpuppies.auth.presentation.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.auth.data.request.OtpRequest
import com.example.theworldofpuppies.auth.data.request.RegistrationRequest
import com.example.theworldofpuppies.auth.domain.AuthApi
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.auth.presentation.login.LoginUiState
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
import kotlin.math.log

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

    private val _referralCode = MutableStateFlow("")
    val referralCode = _referralCode.asStateFlow()

    fun resetState() {
        viewModelScope.launch {
            _registrationUiState.value = RegistrationUiState()
        }
    }


    fun toggleOtpSentState() {
        viewModelScope.launch {
            _registrationUiState.update { it.copy(isOtpSent = !_registrationUiState.value.isOtpSent) }
            Log.i("toggle", "isOtpSent: ${_registrationUiState.value.isOtpSent}")

        }
    }

    fun registerUser(username: String, email: String, phoneNumber: String) {
        viewModelScope.launch {
            _registrationUiState.update { it.copy(isLoading = true) }
            Log.i("toggle", "started")
            val result = authApi.registerUser(RegistrationRequest(phoneNumber, email, username))
            Log.i("toggle", "result came: ${result.onSuccess { it.success.toString() }}")
            result.onSuccess { apiResponse ->
                _registrationUiState.update {
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


    fun verifyRegistration(phoneNumber: String, otp: String, referralCode: String? = null) {
        viewModelScope.launch {
            _registrationUiState.update { it.copy(isLoading = true) }
            val result = authApi.verifyRegistration(OtpRequest(phoneNumber, otp, referralCode))
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
                            val referralCode = userResponse.referralCode
                            val walletBalance = userResponse.walletBalance
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
                                email = userResponse.email,
                                walletBalance = walletBalance,
                                referralCode = referralCode
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