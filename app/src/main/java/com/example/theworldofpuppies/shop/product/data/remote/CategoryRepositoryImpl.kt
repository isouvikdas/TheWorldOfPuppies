package com.example.theworldofpuppies.shop.product.data.remote

import androidx.room.withTransaction
import com.example.theworldofpuppies.core.data.local.Database
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.product.data.local.CategoryEntity
import com.example.theworldofpuppies.shop.product.data.mappers.toCategoryEntity
import com.example.theworldofpuppies.shop.product.domain.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(
    private val db: Database,
    private val productApi: ProductApiImpl
) : CategoryRepository {

    override suspend fun fetchAllCategories(): List<CategoryEntity> = withContext(Dispatchers.IO) {
        db.categoryDao.getCategories()
    }


    override suspend fun fetchAndStoreCategories(): Result<Boolean, NetworkError> {
        return when (val result = productApi.getAllCategories()) {
            is Result.Success -> {
                val apiResponse = result.data
                when {
                    apiResponse.success && apiResponse.data != null -> {
                        val categories =
                            apiResponse.data.map { it.toCategoryEntity() } ?: emptyList()
                        if (categories.isNotEmpty()) {
                            withContext(Dispatchers.IO) {
                                db.withTransaction {
                                    db.categoryDao.upsertAll(categories)
                                    Result.Success(true)
                                }
                            }
                        } else {
                            Result.Error(NetworkError.SERVER_ERROR)
                        }
                    }

                    apiResponse.success && apiResponse.data == null -> {
                        Result.Error(NetworkError.SERIALIZATION)
                    }

                    else -> {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }
                }
            }
            is Result.Error -> Result.Error(result.error)
        }
    }


    override suspend fun clearAllCategories() {
        withContext(Dispatchers.IO) {
            db.withTransaction {
                db.categoryDao.clearAll()
            }
        }
    }
}