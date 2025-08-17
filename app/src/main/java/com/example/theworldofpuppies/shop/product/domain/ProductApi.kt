package com.example.theworldofpuppies.shop.product.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.shop.product.data.remote.dto.CategoryDto
import com.example.theworldofpuppies.shop.product.data.remote.dto.PagedData
import com.example.theworldofpuppies.shop.product.data.remote.dto.ProductDto

interface ProductApi {
    suspend fun getAllProducts(cursor: String? = null):
            Result<ApiResponse<PagedData<ProductDto>>, NetworkError>
    suspend fun getAllFeaturedProducts() : Result<ApiResponse<List<ProductDto>>, NetworkError>
    suspend fun fetchFirstImage(imageId: String):
            Result<ByteArray, NetworkError>
    suspend fun getAllCategories():
            Result<ApiResponse<List<CategoryDto>>, NetworkError>
    suspend fun getAllImagesOfProduct(imageId: String):
            Result<ByteArray, NetworkError>

    suspend fun getProductsByIds(productIds: List<String>)
            : Result<ApiResponse<List<ProductDto>>, NetworkError>
}