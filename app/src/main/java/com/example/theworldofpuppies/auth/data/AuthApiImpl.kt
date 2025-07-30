package com.example.theworldofpuppies.auth.data

import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.auth.data.request.LoginRequest
import com.example.theworldofpuppies.auth.data.request.OtpRequest
import com.example.theworldofpuppies.auth.data.request.RegistrationRequest
import com.example.theworldofpuppies.auth.data.response.UserResponse
import com.example.theworldofpuppies.auth.domain.AuthApi
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApiImpl(
    private val httpClient: HttpClient
) : AuthApi {

    override suspend fun registerUser(registrationRequest: RegistrationRequest)
            : Result<ApiResponse<Unit>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("auth/register/user/send-otp")
            ) {
                setBody(registrationRequest)
            }
        }
    }

    override suspend fun verifyRegistration(otpRequest: OtpRequest)
            : Result<ApiResponse<UserResponse>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("auth/register/user/verify-otp")
            ) {
                setBody(otpRequest)
            }
        }
    }

    override suspend fun loginUser(loginRequest: LoginRequest)
            : Result<ApiResponse<Unit>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("auth/login/user/send-otp")
            ) {
                setBody(loginRequest)
            }
        }
    }

    override suspend fun verifyLogin(otpRequest: OtpRequest)
            : Result<ApiResponse<UserResponse>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("auth/login/user/verify-otp")
            ) {
                setBody(otpRequest)
            }
        }
    }

}