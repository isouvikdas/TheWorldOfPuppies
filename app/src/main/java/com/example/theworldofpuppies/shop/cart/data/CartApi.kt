package com.example.theworldofpuppies.shop.cart.data

import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.cart.data.dto.CartDto
import com.example.theworldofpuppies.shop.cart.data.dto.CartItemDto
import com.example.theworldofpuppies.shop.product.data.remote.dto.ProductDto
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put

class CartApi(
    private val httpClient: HttpClient
) {
    suspend fun addToCart(token: String, productId: String, quantity: Int)
            : Result<ApiResponse<CartItemDto>, NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructUrl("orders/cart-items/add")
            ) {
                header("Authorization", token)
                parameter("productId", productId)
                parameter("quantity", quantity)
            }
        }
    }

    suspend fun updateItemSelection(token: String, cartItemId: String, isSelected: Boolean)
            : Result<ApiResponse<Boolean>, NetworkError> {
        return safeCall {
            httpClient.put(
                urlString = constructUrl("orders/cart-items/isSelect")
            ) {
                header("Authorization", token)
                parameter("cartItemId", cartItemId)
                parameter("isSelected", isSelected)
            }
        }
    }

    suspend fun getUserCart(token: String)
            : Result<ApiResponse<CartDto>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("orders/carts/get")
            ) {
                header("Authorization", token)
            }
        }
    }

    suspend fun getCartItems(token: String)
            : Result<ApiResponse<List<CartItemDto>>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("orders/cart-items/get")
            ) {
                header("Authorization", token)
            }
        }
    }

    suspend fun removeCartItem(token: String, cartItemId: String)
            : Result<ApiResponse<Unit>, NetworkError> {
        return safeCall {
            httpClient.delete(
                urlString = constructUrl("orders/cart-items/remove")
            ) {
                header("Authorization", token)
                parameter("cartItemId", cartItemId)
            }
        }
    }

}
