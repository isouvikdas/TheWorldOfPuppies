package com.example.theworldofpuppies.shop.order.data

import android.util.Log
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.order.data.mappers.toOrder
import com.example.theworldofpuppies.shop.order.domain.Order
import com.example.theworldofpuppies.shop.order.domain.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepositoryImpl(
    private val orderApi: OrderApi,
    private val userRepository: UserRepository
) : OrderRepository {

    override suspend fun createOrder(): Result<Order, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return withContext(Dispatchers.IO) {
            when (val result = orderApi.createOrder(token = token)) {
                is Result.Success -> {
                    val response = result.data
                    Log.i("order", response.data.toString())
                    if (!response.success || response.data == null) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        Result.Success(response.data.toOrder())
                    }
                }

                is Result.Error -> Result.Error(NetworkError.UNKNOWN)
            }
        }
    }

    override suspend fun createPodOrder(): Result<Order, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return withContext(Dispatchers.IO) {
            when (val result = orderApi.createPodOrder(token = token)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success || response.data == null) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        Result.Success(response.data.toOrder())
                    }
                }

                is Result.Error -> Result.Error(NetworkError.UNKNOWN)
            }
        }
    }
}