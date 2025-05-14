package com.example.theworldofpuppies.shop.product.data.remote

import com.example.furryroyals.core.response.ApiResponse
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.product.data.remote.dto.CategoryDto
import com.example.theworldofpuppies.shop.product.data.remote.dto.PagedData
import com.example.theworldofpuppies.shop.product.data.remote.dto.ProductDto
import io.ktor.client.HttpClient

class DummyApi(
    private val httpClient: HttpClient
) {

    private val allProducts = listOf(
        ProductDto(
            id = "6744a4029644e506b119b4bc",
            name = "Adjustable Dog Collar",
            price = 14.99,
            inventory = 140,
            description = "A durable and adjustable collar for dogs",
            categoryName = "Accessories",
            imageIds = emptyList(),
            isFeatured = true
        ),
        ProductDto(
            id = "6744a4029644e506b119b4bd",
            name = "Cat Litter Mat",
            price = 19.99,
            inventory = 95,
            description = "A mat to keep cat litter contained",
            categoryName = "Accessories",
            imageIds = emptyList(),
            isFeatured = true
        ),
        ProductDto(
            id = "6744a4029644e506b119b4be",
            name = "Dog Water Bottle",
            price = 12.5,
            inventory = 180,
            description = "A portable water bottle for dogs",
            categoryName = "Accessories",
            imageIds = emptyList(),
            isFeatured = true
        ),
        ProductDto(
            id = "6744a4029644e506b119b4bf",
            name = "Rabbit Harness",
            price = 17.99,
            inventory = 65,
            description = "A secure harness for rabbits",
            categoryName = "Accessories",
            imageIds = emptyList(),
            isFeatured = true
        )
    )

    fun getAllProducts(cursor: String? = null):
            Result<ApiResponse<PagedData<ProductDto>>, NetworkError> {

        // Simulate pagination: return 3 items per page
        val pageSize = 3
        val startIndex = cursor?.toIntOrNull() ?: 0
        val nextIndex = startIndex + pageSize
        val pagedItems = allProducts.drop(startIndex).take(pageSize)

        val nextCursor = if (nextIndex < allProducts.size) nextIndex.toString() else null

        return Result.Success(
            ApiResponse(
                success = true,
                message = "success",
                data = PagedData(
                    products = pagedItems,
                    nextCursor = nextCursor.toString()
                )
            )
        )
    }

    fun getAllFeaturedProducts():
            Result<ApiResponse<List<ProductDto>>, NetworkError> {

        val featured = allProducts.filter { it.isFeatured == true }

        return Result.Success(
            ApiResponse(
                message = "success",
                data = featured,
                success = true
            )
        )
    }

    fun fetchFirstImage(imageId: String): Result<ByteArray, NetworkError> {
        return Result.Error(NetworkError.SERVER_ERROR) // dummy
    }

    fun getAllCategories(): Result<ApiResponse<List<CategoryDto>>, NetworkError> {
        val categories = listOf(
            CategoryDto(
                name = "Accessories",
                id = "6744a4019644e506b119b4bb",
                productIds = arrayListOf(
                    "6744a4029644e506b119b4bc",
                    "6744a4029644e506b119b4bd",
                    "6744a4029644e506b119b4be",
                    "6744a4029644e506b119b4bf"
                )
            ),
            CategoryDto(
                name = "Grooming",
                id = "6744a3dd9644e506b119b4b6",
                productIds = arrayListOf(
                    "6744a4029644e506b119b4bc",
                    "6744a4029644e506b119b4bd",
                    "6744a4029644e506b119b4be",
                    "6744a4029644e506b119b4bf"
                )
            ),
            CategoryDto(
                name = "Toys",
                id = "6744a41e9644e506b119b4c1",
                productIds = arrayListOf(
                    "6744a4029644e506b119b4bc",
                    "6744a4029644e506b119b4bd",
                    "6744a4029644e506b119b4be",
                    "6744a4029644e506b119b4bf"
                )
            ),
            CategoryDto(
                name = "Food",
                id = "6744a4399644e506b119b4ca",
                productIds = arrayListOf(
                    "6744a4029644e506b119b4bc",
                    "6744a4029644e506b119b4bd",
                    "6744a4029644e506b119b4be",
                    "6744a4029644e506b119b4bf"
                )
            ),
            CategoryDto(
                name = "Furniture",
                id = "6744a44c9644e506b119b4d5",
                productIds = arrayListOf(
                    "6744a4029644e506b119b4bc",
                    "6744a4029644e506b119b4bd",
                    "6744a4029644e506b119b4be",
                    "6744a4029644e506b119b4bf"
                )
            )
        )
        return Result.Success(ApiResponse(message = "success", success = true, data = categories))
    }

    fun getAllImagesOfProduct(imageId: String): Result<ByteArray, NetworkError> {
        return Result.Error(NetworkError.SERVER_ERROR) // dummy
    }
}
