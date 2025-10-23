package com.example.theworldofpuppies.membership.data

import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.membership.domain.PremiumOption
import com.example.theworldofpuppies.membership.domain.PremiumOptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PremiumOptionRepositoryImpl(
    private val premiumOptionApi: PremiumOptionApi,
    private val userRepository: UserRepository
) : PremiumOptionRepository {

    override suspend fun getPremiumOptions(): Result<List<PremiumOption>, NetworkError> {
        return withContext(Dispatchers.IO) {
            when (val result = premiumOptionApi.getPremiumOptions()) {
                is Result.Success -> {
                    val response = result.data
                    when {
                        response.success && response.data != null -> {
                            Result.Success(result.data.data)
                        }
                        response.success && response.data == null -> {
                            Result.Error(NetworkError.SERIALIZATION)
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