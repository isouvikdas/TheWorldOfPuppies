package com.example.theworldofpuppies.shop.order.data

import android.util.Log
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.order.data.mappers.toCharges
import com.example.theworldofpuppies.shop.order.data.mappers.toOrder
import com.example.theworldofpuppies.shop.order.data.mappers.toOrderItem
import com.example.theworldofpuppies.shop.order.domain.Charges
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
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
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

    override suspend fun createCodOrder(): Result<Order, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = orderApi.createCodOrder(token = token)) {
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

    override suspend fun getOrders(): Result<List<Order>, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = orderApi.getOrders(token = token)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success || response.data == null) {
                        return@withContext Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        val orders = response.data.map { (orderDto, orderItemDtos) ->
                            val order = orderDto.toOrder()
                            val orderItems = orderItemDtos.map { it.toOrderItem() }
                            order.copy(orderItems = orderItems)
                        }
                        Result.Success(orders)
                    }
                }
                is Result.Error -> Result.Error(NetworkError.UNKNOWN)
            }
        }
    }

    override suspend fun getOrderById(orderId: String): Result<Order, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = orderApi.getOrderById(token = token, orderId = orderId)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success || response.data == null) {
                        return@withContext Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        val order = response.data.order.toOrder()
                        val orderItems = response.data.orderItems.map { it.toOrderItem() }
                        Result.Success(order.copy(orderItems = orderItems))
                    }
                }
                is Result.Error -> Result.Error(NetworkError.UNKNOWN)
            }
        }
    }

    override suspend fun getCharges(): Result<Charges, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = orderApi.getCharges(token = token)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success || response.data == null) {
                        return@withContext Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        Result.Success(response.data.toCharges())
                    }
                }
                is Result.Error -> Result.Error(NetworkError.UNKNOWN)
            }
        }
    }
}