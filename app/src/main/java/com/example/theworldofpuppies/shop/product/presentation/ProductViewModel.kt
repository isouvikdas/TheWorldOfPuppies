package com.example.theworldofpuppies.shop.product.presentation

import android.util.Log
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.product.data.mappers.toCategory
import com.example.theworldofpuppies.shop.product.data.mappers.toProduct
import com.example.theworldofpuppies.shop.product.domain.CategoryRepository
import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.shop.product.domain.ProductRepository
import com.example.theworldofpuppies.shop.product.presentation.product_detail.ProductDetailState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _productListState = MutableStateFlow(ProductListState())
    val productListState: StateFlow<ProductListState> = _productListState.asStateFlow()

    private val _categoryListState = MutableStateFlow(CategoryListState())
    val categoryListState: StateFlow<CategoryListState> = _categoryListState.asStateFlow()

    private val _featuredProductListState = MutableStateFlow(FeaturedProductListState())
    val featuredProductListState: StateFlow<FeaturedProductListState> = _featuredProductListState.asStateFlow()

    private val _productDetailState = MutableStateFlow(ProductDetailState())
    val productDetailState: StateFlow<ProductDetailState> = _productDetailState.asStateFlow()

    init {
        clearCachedData()
        fetchCategories()
        fetchNextPage()
        fetchFeaturedProducts()
    }

    fun setProduct(product: Product) {
        _productDetailState.value = ProductDetailState(product = product)
    }

    private fun clearCachedData() {
        viewModelScope.launch {
            async {
                categoryRepository.clearAllCategories()
                productRepository.clearAllProducts()
            }.await()
        }
    }

    private val alreadyRequestedIds = mutableSetOf<String>()

    fun fetchImagesIfNeeded(product: Product) {
        product.imageIds.forEach { imageId ->
            if (!_productDetailState.value.fetchedImageIds.contains(imageId) && !alreadyRequestedIds.contains(imageId)) {
                alreadyRequestedIds.add(imageId)
                getProductImages(imageId)
            }
        }
    }


    fun getProductImages(imageId: String) {
        viewModelScope.launch {
            _productDetailState.update { it.copy(isImageLoading = true) }
            val byteArrayImage = productRepository.getProductImages(imageId)
            if (byteArrayImage != null) {
                if (byteArrayImage.isNotEmpty()) {
                    val base64Image = Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
                    val product = productDetailState.value.product?.copy(isImagesFetched = true)
                    _productDetailState.update {
                        it.copy(
                            errorMessage = null,
                            images = it.images + base64Image,
                            fetchedImageIds = it.fetchedImageIds + imageId,
                            isImageLoading = false,
                            product = product
                        )
                    }
                } else {
                    _productDetailState.update {
                        it.copy(
                            errorMessage = "Fetching images failed",
                            isImageLoading = false
                        )
                    }
                }
            }
        }
    }


    fun getProductDetails(productId: String) {
        viewModelScope.launch {
            _productDetailState.update { it.copy(isLoading = true) }
            val productEntity = productRepository.getProductDetails(productId)
            if (productEntity != null) {
                _productDetailState.update {
                    it.copy(
                        errorMessage = null,
                        product = productEntity.toProduct(),
                        isLoading = false
                    )
                }
            } else {
                _productDetailState.update {
                    it.copy(
                        errorMessage = "Fetching product details failed",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchFeaturedProducts() {
        if (_featuredProductListState.value.isLoading) return
        viewModelScope.launch {
            try {
                _featuredProductListState.update { it.copy(isLoading = true, errorMessage = null) }

                when (val result = productRepository.fetchAndStoreFeaturedProducts()) {
                    is Result.Success -> {
                        val productList =
                            productRepository.getAllFeaturedProducts().map { it.toProduct() }
                        if (productList.isNullOrEmpty()) {
                            Log.e("toggle", "empty")
                        } else {
                            productList.forEach { Log.i("toggle", it.toString()) }
                        }
                        _featuredProductListState.update { it.copy(productList = productList) }
//                        val product = featuredProductListState.value.productList.find {it.imageIds.isNotEmpty()}
//                        val imageIds = product?.imageIds ?: emptyList()
//                        if (imageIds.isNotEmpty()) {
//                            imageIds.forEach {
//                                Log.i("imageId", it.toString())
//                            }
//                        } else {
//                            Log.i("imageId", "imageIds are empty")
//                        }
                    }

                    is Result.Error -> _featuredProductListState.update { it.copy(errorMessage = result.error.toString()) }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching Featured Products.")
            } finally {
                _featuredProductListState.update { it.copy(isLoading = false) }
            }
        }
    }


    fun fetchCategories() {
        if (_categoryListState.value.isLoading) return
        viewModelScope.launch {
            try {
                _categoryListState.update { it.copy(isLoading = true, errorMessage = null) }

                val isListReceived = categoryRepository.fetchAndStoreCategories()
                Log.i("category in viewmodel", isListReceived.toString())
                if (isListReceived) {
                    val categories = categoryRepository.fetchAllCategories()
                    categories.forEach {
                        Log.i("category in viewmodel", it.name)
                    }
                    _categoryListState.update {
                        it.copy(
                            categoryList = it.categoryList + categories.map { categoryEntity ->
                                categoryEntity.toCategory()
                            },
                        )
                    }
                } else {
                    _categoryListState.update {
                        it.copy(errorMessage = "Failed to fetch categories")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching categories")
            } finally {
                _categoryListState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun fetchNextPage() {
        if (_productListState.value.endOfPaginationReached || _productListState.value.isLoading) return
        viewModelScope.launch {
            try {
                _productListState.update { it.copy(isLoading = true, errorMessage = null) }

                val nextCursor = productRepository.fetchAndStoreProducts(
                    _productListState.value.currentCursor
                )

                val newProducts =
                    productRepository.fetchLastFetchedPage(_productListState.value.localCursor)

                if (newProducts.isNotEmpty()) {
                    _productListState.update {
                        it.copy(
                            productList = it.productList + newProducts.map { productEntity ->
                                productEntity.toProduct()
                            },
                            currentCursor = nextCursor,
                            localCursor = newProducts.last().localId,
                        )
                    }
                } else {
                    _productListState.update {
                        it.copy(
                            endOfPaginationReached = true
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching products")
            } finally {
                _productListState.update { it.copy(isLoading = false) }
            }
        }
    }
}
