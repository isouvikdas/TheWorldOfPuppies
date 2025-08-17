package com.example.theworldofpuppies.shop.order.data

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.shop.order.data.response.ChargesDto
import com.example.theworldofpuppies.shop.order.data.response.OrderDto
import com.example.theworldofpuppies.shop.order.data.response.OrderFetchResponse
import com.example.theworldofpuppies.shop.order.data.response.OrderItemDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post

class OrderApi(
    private val httpClient: HttpClient
) {
    suspend fun createOrder(token: String)
            : Result<ApiResponse<OrderDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("orders/order/create")
            ) {
                header("Authorization", token)
            }
        }
    }

    suspend fun createCodOrder(token: String)
            : Result<ApiResponse<OrderDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("orders/order/pod/create")
            ) {
                header("Authorization", token)
            }
        }
    }

    suspend fun getOrders(token: String)
            : Result<ApiResponse<List<OrderFetchResponse>>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("orders/get")
            ) {
                header("Authorization", token)
            }
        }
    }

    suspend fun getOrderById(token: String, orderId: String)
            : Result<ApiResponse<OrderFetchResponse>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("orders/order/get")
            ) {
                header("Authorization", token)
                parameter("orderId", orderId)
            }
        }
    }

    suspend fun getCharges(token: String): Result<ApiResponse<ChargesDto>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("orders/charges")
            ) {
                header("Authorization", token)
            }
        }
    }

}