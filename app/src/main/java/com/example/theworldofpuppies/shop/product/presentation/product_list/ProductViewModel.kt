package com.example.theworldofpuppies.shop.product.presentation.product_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.SearchUiState
import com.example.theworldofpuppies.core.presentation.util.Event
import com.example.theworldofpuppies.shop.product.data.mappers.toCategory
import com.example.theworldofpuppies.shop.product.data.mappers.toProduct
import com.example.theworldofpuppies.shop.product.domain.Category
import com.example.theworldofpuppies.shop.product.domain.CategoryRepository
import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.shop.product.domain.ProductRepository
import com.example.theworldofpuppies.shop.product.domain.util.ListType
import com.example.theworldofpuppies.shop.product.domain.util.SortProduct
import com.example.theworldofpuppies.shop.product.presentation.product_detail.ProductDetailState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(FlowPreview::class)
class ProductViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val authEventManager: AuthEventManager
) : ViewModel() {

    private val _productListState = MutableStateFlow(ProductListState())
    val productListState: StateFlow<ProductListState> = _productListState.asStateFlow()

    private val _categoryListState = MutableStateFlow(CategoryListState())
    val categoryListState: StateFlow<CategoryListState> = _categoryListState.asStateFlow()

    private val _featuredProductListState = MutableStateFlow(FeaturedProductListState())
    val featuredProductListState: StateFlow<FeaturedProductListState> =
        _featuredProductListState.asStateFlow()

    private val _isShopHomeRefreshing = MutableStateFlow(false)
    val isShopHomeRefreshing = _isShopHomeRefreshing.asStateFlow()

    private val _isCategoryRefreshing = MutableStateFlow(false)
    val isCategoryRefreshing = _isCategoryRefreshing.asStateFlow()

    private val _isProductRefreshing = MutableStateFlow(false)
    val isProductRefreshing = _isProductRefreshing.asStateFlow()

    private val _productDetailState = MutableStateFlow(ProductDetailState())
    val productDetailState: StateFlow<ProductDetailState> = _productDetailState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun forceLoadShopHomeScreen() {
        viewModelScope.launch {
            _isShopHomeRefreshing.value = true
            clearCachedData()
            fetchCategories()
            fetchNextPage(isForceRefresh = true)
            fetchFeaturedProducts()
            delay(1000)
            _isShopHomeRefreshing.value = false
        }
    }

    fun forceLoadCategoryScreen() {
        viewModelScope.launch {
            _isCategoryRefreshing.value = true
            clearCategory()
            fetchCategories()
            delay(1000)
            _isCategoryRefreshing.value = false
        }
    }

    fun forceLoadProductScreen() {
        viewModelScope.launch {
            _isProductRefreshing.value = true
            clearProducts()
            fetchNextPage(true)
            fetchFeaturedProducts()
            delay(1000)
            _isProductRefreshing.value = false
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchedProductFlow = searchText
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            flow {
                try {
                    _isSearching.emit(true)
                    val filtered = if (query.isBlank()) {
                        emptyList()
                    } else {
                        delay(300L)
                        productListState.value.productList.filter {
                            it.doesMatchSearchQuery(query)
                        }
                    }
                    emit(filtered)
                } finally {
                    _isSearching.emit(false)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    val searchUiState = combine(
        searchText,
        searchedProductFlow,
        isSearching
    ) { query, results, loading ->
        SearchUiState(
            query = query,
            results = results,
            isSearching = loading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SearchUiState()
    )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun setSelectedCategory(category: Category?) {
        _selectedCategory.update { category }
    }

    private val _sortOption = MutableStateFlow(SortProduct.RECOMMENDED)
    val sortOption = _sortOption.asStateFlow()

    private val _isPaginationEnabled = MutableStateFlow(false)
    val isPaginationEnabled = _isPaginationEnabled.asStateFlow()

    fun setSortOption(option: SortProduct) {
        _sortOption.update { option }
    }

    init {
        observeAuthEvents()
        viewModelScope.launch {
            clearCachedData()
            if (userRepository.isLoggedIn()) {
                fetchCategories()
                fetchNextPage()
                fetchFeaturedProducts()
            }
        }
    }

    fun setListType(type: ListType) {
        _productListState.update { it.copy(listType = type) }
    }

    fun getProductsByType(type: ListType): List<Product> {
        return when (type) {
            ListType.ALL -> {
                _isPaginationEnabled.value = true
                _productListState.value.productList.filter { it.isFeatured == false }
            }

            ListType.FEATURED -> {
                _isPaginationEnabled.value = false
                _productListState.value.productList.filter { it.isFeatured == true }
            }

            ListType.NEW -> {
                _isPaginationEnabled.value = false
                _productListState.value.productList
            }

            else -> emptyList()
        }
    }

    val filteredSortedProducts = combine(
        productListState,
        selectedCategory,
        sortOption
    ) { productListState, selectedCategory, sortOption ->
        var filtered = getProductsByType(productListState.listType)
        if (selectedCategory != null) {
            filtered = filtered.filter { it.categoryName == selectedCategory.name }
        }
        when (sortOption) {
            SortProduct.HIGH_TO_LOW -> filtered.sortedByDescending { it.price }
            SortProduct.LOW_TO_HIGH -> filtered.sortedBy { it.price }
            SortProduct.HIGHEST_RATED -> filtered.sortedByDescending { it.averageStars }
            SortProduct.LATEST -> filtered.sortedBy { it.isFeatured }
            else -> filtered
        }
    }

    fun setProduct(product: Product) {
        _productDetailState.value = ProductDetailState(product = product)
    }

    private suspend fun clearCachedData() {
        withContext(Dispatchers.IO) {
            categoryRepository.clearAllCategories()
            productRepository.clearAllProducts()
        }
    }

    private suspend fun clearCategory() {
        withContext(Dispatchers.IO) {
            categoryRepository.clearAllCategories()
        }
    }

    private suspend fun clearProducts() {
        withContext(Dispatchers.IO) {
            productRepository.clearAllProducts()
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
                    }

                    is Result.Error -> _featuredProductListState.update { it.copy(errorMessage = result.error) }
                }
            } catch (e: Exception) {
                Timber.Forest.e(e, "Error fetching Featured Products.")
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

                when (val result = categoryRepository.fetchAndStoreCategories()) {
                    is Result.Success -> {
                        val categories = categoryRepository.fetchAllCategories()
                        categories.forEach {
                            Log.i("category in viewmodel", it.name)
                        }
                        _categoryListState.update {
                            it.copy(
                                categoryList = categories.map { categoryEntity ->
                                    categoryEntity.toCategory()
                                },
                            )
                        }
                    }

                    is Result.Error -> {
                        _categoryListState.update { it.copy(errorMessage = result.error) }
                    }
                }
            } catch (e: Exception) {
                Timber.Forest.e(e, "Error fetching categories")
            } finally {
                _categoryListState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun fetchNextPage(isForceRefresh: Boolean = false) {
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
                            productList = if (!isForceRefresh) {
                                it.productList + newProducts.map { productEntity ->
                                    productEntity.toProduct()
                                }
                            } else {
                                newProducts.map { productEntity ->
                                    productEntity.toProduct()
                                }
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
                Timber.Forest.e(e, "Error fetching products")
            } finally {
                _productListState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun observeAuthEvents() {
        viewModelScope.launch {
            authEventManager.events.collect { event ->
                if (event is Event.LoggedIn) {
                    clearCachedData()
                    fetchCategories()
                    fetchNextPage()
                    fetchFeaturedProducts()
                }
            }
        }
    }
}