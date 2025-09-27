package com.example.theworldofpuppies.shop.product.data.remote

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.shop.product.data.remote.dto.CategoryDto
import com.example.theworldofpuppies.shop.product.data.remote.dto.ImageDto
import com.example.theworldofpuppies.shop.product.data.remote.dto.PagedData
import com.example.theworldofpuppies.shop.product.data.remote.dto.ProductDto
import com.example.theworldofpuppies.shop.product.domain.ProductApi
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductApiImpl(
    private val httpClient: HttpClient
): ProductApi {
    override suspend fun getAllProducts(cursor: String?):
            Result<ApiResponse<PagedData<ProductDto>>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("products/all")
            ) {
                parameter("cursor", cursor)
            }
        }
    }

    override suspend fun getAllFeaturedProducts() : Result<ApiResponse<List<ProductDto>>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("products/featured")
            )
        }
    }
    override suspend fun getAllCategories():
            Result<ApiResponse<List<CategoryDto>>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("products/categories/all")
            )
        }
    }

    override suspend fun getProductsByIds(productIds: List<String>)
            : Result<ApiResponse<List<ProductDto>>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("products/productIds")
            ) {
                parameter("productIds", productIds)
            }
        }
    }
}