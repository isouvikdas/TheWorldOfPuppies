package com.example.theworldofpuppies.shop.product.data.remote

import androidx.room.withTransaction
import com.example.theworldofpuppies.core.data.local.Database
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.domain.util.onError
import com.example.theworldofpuppies.core.domain.util.onSuccess
import com.example.theworldofpuppies.shop.product.data.local.ProductEntity
import com.example.theworldofpuppies.shop.product.data.mappers.toProductEntity
import com.example.theworldofpuppies.shop.product.domain.ProductApi
import com.example.theworldofpuppies.shop.product.domain.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class ProductRepositoryImpl(
    private val productApi: ProductApi,
    private val db: Database,
) : ProductRepository {

    override suspend fun getAllFeaturedProducts(): List<ProductEntity> {
        var products = emptyList<ProductEntity>()
        withContext(Dispatchers.IO) {
            products = db.productDao.getFeaturedProducts()
        }
        return products
    }

    override suspend fun fetchAndStoreFeaturedProducts(): Result<Boolean, NetworkError> {
        return withContext(Dispatchers.IO) {
            when (val result = productApi.getAllFeaturedProducts()) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        val products = response.data ?: emptyList()
                        val existingFeaturedProductIds = db.productDao.getFeaturedProductIds()
                        val newProducts = products.filter { it.id !in existingFeaturedProductIds }
                        if (newProducts.isNotEmpty()) {
                            storeProductsWithCaching(newProducts.map { it.toProductEntity() })
                            Result.Success(true)
                        } else {
                            Result.Error(NetworkError.UNKNOWN)
                        }
                    }
                }

                is Result.Error -> Result.Error(NetworkError.UNKNOWN)
            }
        }
    }


    override suspend fun getProductDetails(productId: String): ProductEntity? {
        var productEntity: ProductEntity?
        try {
            withContext(Dispatchers.IO) {
                productEntity = db.productDao.getProductById(productId)
            }
        } catch (e: Exception) {
            throw e
        }
        return productEntity
    }

    override suspend fun fetchLastFetchedPage(localCursor: Long) = withContext(Dispatchers.IO) {
        db.productDao.getProductsAfterCursor(localCursor)
    }

    override suspend fun fetchAndStoreProducts(cursor: String?): String? {
        try {
            val result = productApi.getAllProducts(cursor)

            result.onSuccess { apiResponse ->
                if (!apiResponse.success) {
                    throw IOException(apiResponse.message)
                }

                val pagedData = apiResponse.data
                val products = pagedData?.products?.map { it.toProductEntity() } ?: emptyList()

                if (products.isNotEmpty()) {
                    storeProductsWithCaching(products)
                }

                return pagedData?.nextCursor
            }.onError { error ->
                throw IOException("API Error: $error")
            }

        } catch (e: IOException) {
            Timber.e(e, "Network or API error occurred.")
            return null
        } catch (e: Exception) {
            Timber.e(e, "Unexpected error occurred.")
            return null
        }
        return null
    }

    private suspend fun storeProductsWithCaching(products: List<ProductEntity>) {
        db.withTransaction {
            db.productDao.upsertAll(products)
        }
    }

    override suspend fun clearAllProducts() {
        withContext(Dispatchers.IO) {
            db.withTransaction {
                db.productDao.clearAll()
            }
        }
    }

}