package com.example.theworldofpuppies.refer_earn.data

import android.util.Log
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.refer_earn.data.remote.ReferEarnApi
import com.example.theworldofpuppies.refer_earn.domain.ReferEarnRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReferEarnRepositoryImpl(
    private val userRepository: UserRepository,
    private val api: ReferEarnApi
): ReferEarnRepository {

    override suspend fun getReferralCode(): Result<String, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        return withContext(Dispatchers.IO) {
            when (val result = api.generateReferralCode(token)) {
                is Result.Success -> {
                    val response = result.data
                    Log.i("referral", response.data.toString())
                    when {
                        response.success && response.data != null -> {
                            Result.Success(response.data)
                        }
                        response.success && response.data == null -> {
                            Result.Error(NetworkError.SERVER_ERROR)
                        }
                        else -> {
                            Result.Error(NetworkError.UNKNOWN)
                        }
                    }
                }
                is Result.Error -> {
                    Result.Error(result.error)
                }
            }
        }

    }

    override suspend fun getWalletBalance(): Result<Double, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        return withContext(Dispatchers.IO) {
            when (val result = api.getWalletBalance(token)) {
                is Result.Success -> {
                    val response = result.data
                    when {
                        response.success && response.data != null -> {
                            Result.Success(response.data)
                        }
                        response.success && response.data == null -> {
                            Result.Error(NetworkError.SERVER_ERROR)
                        }
                        else -> {
                            Result.Error(NetworkError.UNKNOWN)
                        }
                    }
                }
                is Result.Error -> {
                    Result.Error(result.error)
                }
            }
        }

    }
}