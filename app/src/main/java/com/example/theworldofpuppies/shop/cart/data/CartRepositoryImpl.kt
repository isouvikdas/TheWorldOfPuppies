package com.example.theworldofpuppies.shop.cart.data

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.example.theworldofpuppies.core.data.local.Database
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.domain.util.onError
import com.example.theworldofpuppies.core.domain.util.onSuccess
import com.example.theworldofpuppies.shop.cart.data.mappers.toCart
import com.example.theworldofpuppies.shop.cart.data.mappers.toCartItem
import com.example.theworldofpuppies.shop.cart.domain.Cart
import com.example.theworldofpuppies.shop.cart.domain.CartItem
import com.example.theworldofpuppies.shop.cart.domain.CartRepository
import com.example.theworldofpuppies.shop.product.data.local.ProductEntity
import com.example.theworldofpuppies.shop.product.data.mappers.toProduct
import com.example.theworldofpuppies.shop.product.data.mappers.toProductEntity
import com.example.theworldofpuppies.shop.product.domain.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class CartRepositoryImpl(
    private val cartApi: CartApi,
    private val userRepository: UserRepository,
    private val db: Database,
    private val productRepository: ProductRepository,
    private val context: Context
) : CartRepository {

    override suspend fun addToCart(
        productId: String,
        quantity: Int,
        isNewItem: Boolean
    ): Result<CartItem, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return withContext(Dispatchers.IO) {
            when (val result =
                cartApi.addToCart(token = token, productId = productId, quantity = quantity)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        response.data?.let {
                            val cartItem = it.toCartItem()
                            val finalCartItem = if (isNewItem) {
                                val product = db.productDao.getProductById(cartItem.productId)?.toProduct()
                                cartItem.copy(product = product)
                            } else {
                                cartItem
                            }
                            Result.Success(finalCartItem)

                        } ?: Result.Error(NetworkError.SERVER_ERROR)
                    }
                }

                is Result.Error -> {
                    Result.Error(NetworkError.UNKNOWN)
                }
            }
        }
    }

    override suspend fun removeCartItem(cartItemId: String): Result<Boolean, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return withContext(Dispatchers.IO) {
            when(val result = cartApi.removeCartItem(token = token, cartItemId = cartItemId)) {
                is Result.Success -> {
                    val response = result.data
                    if (response.success) {
                        Result.Success(true)
                    } else {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }
                }
                is Result.Error -> {
                    Result.Error(NetworkError.UNKNOWN)
                }
            }

        }
    }

    override suspend fun getUserCart(): Result<Cart, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return withContext(Dispatchers.IO) {
            when (val result = cartApi.getUserCart(token = token)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        response.data?.let {
                            Result.Success(it.toCart())
                        } ?: Result.Error(NetworkError.SERVER_ERROR)
                    }
                }

                is Result.Error -> {
                    Result.Error(NetworkError.UNKNOWN)
                }
            }
        }
    }

    override suspend fun updateItemSelection(
        cartItemId: String,
        isSelected: Boolean
    ): Result<Boolean, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return withContext(Dispatchers.IO) {
            when (val result = cartApi.updateItemSelection(
                token = token,
                cartItemId = cartItemId,
                isSelected = isSelected
            )) {
                is Result.Success -> {
                    val response = result.data
                    if (response.success) {
                        Result.Success(true)
                    } else {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }
                }

                is Result.Error -> Result.Error(NetworkError.SERVER_ERROR)
            }
        }
    }

    override suspend fun getCartItems(): Result<List<CartItem>, NetworkError> {
        val token = userRepository.getToken()
        if (token.isNullOrEmpty()) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return withContext(Dispatchers.IO) {
            when (val result = cartApi.getCartItems(token = token)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        val cartItems = response.data?.map { it.toCartItem() } ?: emptyList()
                        val productIdsToBeFetched = cartItems.map { it.productId }
                        val (dbProducts, missingProductIds) = fetchProductsFromDatabase(
                            productIdsToBeFetched
                        )
                        val apiProducts = if (missingProductIds.isNotEmpty()) {
                            fetchProductsFromApi(missingProductIds)
                        } else {
                            emptyList()
                        }
                        val allProducts = dbProducts + apiProducts
                        cartItems.map { cartItem ->
                            val matchedProduct =
                                allProducts.find { it.id == cartItem.productId }
                            matchedProduct?.let {
                                cartItem.apply {
                                    product = it.toProduct()
                                    price = it.discountedPrice
                                    totalPrice = it.discountedPrice.times(cartItem.quantity)
                                }
                            }
                        }
                        cartItems.forEach {
                            Log.i("getUserCart", it.toString())
                        }
                        Result.Success(cartItems)
                    }
                }

                is Result.Error -> {
                    Log.e("getCartItems", "API call failed: ${result.error}")
                    Result.Error(NetworkError.UNKNOWN)
                }
            }
        }
    }

    private suspend fun fetchProductsFromDatabase(productIds: List<String>): Pair<List<ProductEntity>, List<String>> {
        val dbProducts = mutableListOf<ProductEntity>()
        val missingProductIds = mutableListOf<String>()

        db.withTransaction {
            productIds.forEach { productId ->
                val product = db.productDao.getProductById(productId)
                if (product != null) {
                    dbProducts.add(product)
                } else {
                    missingProductIds.add(productId)
                }
            }
        }
        return dbProducts to missingProductIds
    }

    private suspend fun fetchProductsFromApi(productIds: List<String>): List<ProductEntity> {
        val result = cartApi.getProductsByIds(productIds)
        result.onSuccess { apiResponse ->
            if (!apiResponse.success) {
                throw IOException(apiResponse.message)
            }
            val products =
                fetchFirstImage(apiResponse.data?.map { it.toProductEntity() } ?: emptyList())
            return products
        }.onError { error ->
            throw IOException("Failed loading products: $error")
        }
        return emptyList()
    }

    private suspend fun fetchFirstImage(products: List<ProductEntity>): List<ProductEntity> {
        val updatedProducts = withContext(Dispatchers.IO) {
            products.map { product ->
                async {
                    try {
                        val firstImageUri = productRepository.cacheFirstImage(product, context)
                        product.copy(firstImageUri = firstImageUri)
                    } catch (e: Exception) {
                        Timber.e(e, "Error caching image for the product: ${product.id}")
                        product
                    }
                }
            }.awaitAll()
        }
        return updatedProducts
    }
}