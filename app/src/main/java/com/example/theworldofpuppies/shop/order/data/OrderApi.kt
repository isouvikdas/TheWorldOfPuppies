package com.example.theworldofpuppies.shop.order.data

import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.order.data.response.OrderDto
import io.ktor.client.HttpClient
import io.ktor.client.request.header
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

    suspend fun createPodOrder(token: String)
            : Result<ApiResponse<OrderDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("orders/order/pod/create")
            ) {
                header("Authorization", token)
            }
        }
    }

}