package com.example.theworldofpuppies.membership.data

import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.membership.data.mappers.toPremiumOptionOrder
import com.example.theworldofpuppies.membership.domain.PremiumOption
import com.example.theworldofpuppies.membership.domain.PremiumOptionOrder
import com.example.theworldofpuppies.membership.domain.PremiumOptionRepository
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest
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

    override suspend fun buyPremium(premiumOptionId: String)
            : Result<PremiumOptionOrder, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = premiumOptionApi.buyPremium(token, premiumOptionId)) {
                is Result.Success -> {
                    val response = result.data
                    when {
                        response.success && response.data != null -> {
                            Result.Success(response.data.toPremiumOptionOrder())
                        }

                        response.success && response.data == null -> {
                            Result.Error(NetworkError.SERIALIZATION)
                        }

                        else -> {
                            Result.Error(NetworkError.SERVER_ERROR)
                        }
                    }
                }

                is Result.Error -> Result.Error(result.error)
            }
        }
    }

    override suspend fun verifyPayment(paymentVerificationRequest: PaymentVerificationRequest)
            : Result<Boolean, NetworkError> {
        return withContext(Dispatchers.IO) {
            when (val result = premiumOptionApi.verifyPayment(paymentVerificationRequest)) {
                is Result.Success -> {
                    val response = result.data
                    if (response.success) {
                        Result.Success(true)
                    } else {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }
                }

                is Result.Error -> {
                    Result.Error(result.error)
                }
            }
        }
    }

    override suspend fun getPremiumOptionOrder(premiumOptionOrderId: String)
            : Result<PremiumOptionOrder, NetworkError> {
        return withContext(Dispatchers.IO) {
            when (val result = premiumOptionApi.getPremiumOptionOrder(premiumOptionOrderId)) {
                is Result.Success -> {
                    val response = result.data
                    when {
                        response.success && response.data != null -> {
                            Result.Success(
                                response.data
                                    .toPremiumOptionOrder()
                            )
                        }

                        response.success && response.data == null -> {
                            Result.Error(NetworkError.SERIALIZATION)
                        }

                        else -> {
                            Result.Error(NetworkError.SERVER_ERROR)
                        }
                    }
                }

                is Result.Error -> Result.Error(result.error)
            }
        }
    }
}