package com.example.theworldofpuppies.shop.product.data.remote

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.example.theworldofpuppies.core.data.local.Database
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.domain.util.onError
import com.example.theworldofpuppies.core.domain.util.onSuccess
import com.example.theworldofpuppies.shop.product.data.local.ProductEntity
import com.example.theworldofpuppies.shop.product.data.mappers.toProductEntity
import com.example.theworldofpuppies.shop.product.domain.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProductRepositoryImpl(
    private val productApi: ProductApi,
    private val db: Database,
    private val context: Context
) : ProductRepository {

    override suspend fun getAllFeaturedProducts(): List<ProductEntity> {
        var products = emptyList<ProductEntity>()
        withContext(Dispatchers.IO) {
            products = db.productDao.getFeaturedProducts()
            if (products.isNotEmpty()) {
                products.forEach { Log.i("toggle", it.toString()) }
            } else {
                Log.i("toggle", "List is empty")
            }
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
                        if (newProducts.isNullOrEmpty()) {
                            newProducts.forEach { Log.e("toggle", "newProduct is empty") }
                        } else {
                            newProducts.forEach { Log.e("toggle", it.toString()) }
                        }
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

    override suspend fun getProductImages(imageId: String): ByteArray? {
        var image: ByteArray? = null
        try {
            val result = productApi.getAllImagesOfProduct(imageId)
            result.onSuccess {
                image = it
            }.onError { error ->
                throw IOException("Network error: $error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return image
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
        val updatedProducts = withContext(Dispatchers.IO) {
            products.map { product ->
                async {
                    try {
                        val firstImageUri = cacheFirstImage(product, context)
                        product.copy(firstImageUri = firstImageUri)
                    } catch (e: Exception) {
                        Timber.e(e, "Error caching image for product ID: ${product.id}")
                        product
                    }
                }
            }.awaitAll()
        }

        db.withTransaction {
            db.productDao.upsertAll(updatedProducts)
        }
    }


    override suspend fun cacheFirstImage(productEntity: ProductEntity, context: Context): String {
        try {
            val imageResult = productEntity.firstImageId?.let { productApi.fetchFirstImage(it) }
            var cachedFilePath: String? = null

            imageResult?.onSuccess { byteArray ->
                val cachedFile =
                    saveToDiskCache(byteArray, productEntity.id, context.cacheDir)
                cachedFilePath = cachedFile?.absolutePath

            }?.onError { error ->
                throw RuntimeException("Network error: $error")
            }

            return cachedFilePath ?: throw IllegalStateException("Failed to cache the image")
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


    private suspend fun saveToDiskCache(
        byteArray: ByteArray,
        uniqueId: String,
        cacheDir: File
    ): File? {
        val file = File(cacheDir, "$uniqueId.jpg")
        return try {
            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(byteArray)
                }
            }
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun clearAllProducts() {
        withContext(Dispatchers.IO) {
            db.withTransaction {
                db.productDao.clearAll()
                clearImageCached()
            }
        }
    }

    private fun clearImageCached() {
        val cacheDir = context.cacheDir
        if (cacheDir.exists()) {
            val files = cacheDir.listFiles()
            files?.forEach { file ->
                if (file.isFile && file.name.endsWith(".jpg")) {
                    file.delete()
                }
            }
        }
    }
}