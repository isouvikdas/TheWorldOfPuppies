package com.example.theworldofpuppies.auth.domain

import com.example.furryroyals.core.response.ApiResponse
import com.example.theworldofpuppies.auth.data.request.LoginRequest
import com.example.theworldofpuppies.auth.data.request.OtpRequest
import com.example.theworldofpuppies.auth.data.request.RegistrationRequest
import com.example.theworldofpuppies.auth.data.response.UserResponse
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface AuthApi {
    suspend fun registerUser(registrationRequest: RegistrationRequest): Result<ApiResponse<Unit>, NetworkError>
    suspend fun verifyRegistration(otpRequest: OtpRequest): Result<ApiResponse<UserResponse>, NetworkError>
    suspend fun loginUser(loginRequest: LoginRequest): Result<ApiResponse<Unit>, NetworkError>
    suspend fun verifyLogin(otpRequest: OtpRequest): Result<ApiResponse<UserResponse>, NetworkError>
}