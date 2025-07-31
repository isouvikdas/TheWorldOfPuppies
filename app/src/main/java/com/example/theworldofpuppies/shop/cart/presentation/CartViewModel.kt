package com.example.theworldofpuppies.shop.cart.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.Event
import com.example.theworldofpuppies.shop.cart.domain.CartItem
import com.example.theworldofpuppies.shop.cart.domain.CartRepository
import com.example.theworldofpuppies.shop.order.presentation.utils.OrderEvent
import com.example.theworldofpuppies.shop.order.presentation.utils.OrderEventManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val orderEventManager: OrderEventManager,
    private val authEventManager: AuthEventManager
) : ViewModel() {
    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    init {
        getUserCart()
        observeAuthEvents()
        observeOrderEvents()
    }

    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun addToCart(productId: String, quantity: Int, isItProductScreen: Boolean) {
        viewModelScope.launch {
            if (isItProductScreen) {
                if (quantity < 1) {
                    showToast("Please select at least 1")
                    return@launch
                }
            }
            _cartUiState.update { it.copy(errorMessage = null, isLoading = true) }
            try {
                val cartItems = cartUiState.value.cartItems.orEmpty()
                val existingItem = cartItems.find { it.productId == productId }
                val isNewItem = existingItem == null

                when (
                    val cartResult = cartRepository.addToCart(
                        productId = productId,
                        quantity = quantity,
                        isNewItem = isNewItem
                    )
                ) {
                    is Result.Success -> {
                        val cartItem = cartResult.data
                        val updatedItems = if (isNewItem) {
                            cartItems + cartItem
                        } else {
                            updateExistingItemQuantity(
                                cartItems = cartItems,
                                quantity = quantity,
                                productId = productId
                            )
                        }

                        val totalSelectedItems = updatedItems.count { it.isSelected == true }
                        val cartTotal = calculateCartTotal(updatedItems)

                        _cartUiState.update {
                            it.copy(
                                cartItems = updatedItems,
                                totalSelectedItems = totalSelectedItems,
                                cartTotal = cartTotal
                            )
                        }
                        showToast("Item added successfully")
                    }

                    is Result.Error -> {
                        _cartUiState.update { it.copy(errorMessage = cartResult.error.toString()) }
                    }
                }
            } catch (e: Exception) {
                _cartUiState.update { it.copy(errorMessage = e.message) }
            } finally {
                _cartUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun removeCartItem(productId: String) {
        viewModelScope.launch {
            _cartUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val cartItems = cartUiState.value.cartItems.orEmpty()
                val cartItem = cartItems.find { it.productId == productId }
                val cartItemId = cartItem?.id

                if (!cartItemId.isNullOrEmpty()) {
                    when (val result = cartRepository.removeCartItem(cartItemId)) {
                        is Result.Success -> {
                            if (result.data) {
                                val updatedCartItems = cartItems.filterNot { it.productId == productId }

                                val updatedCart = cartUiState.value.cart?.let { cart ->
                                    val updatedIds = cart.cartItemIds.filterNot { it == cartItemId }
                                    cart.copy(cartItemIds = updatedIds)
                                }

                                val totalSelectedItems = updatedCartItems.count { it.isSelected == true }
                                val cartTotal = calculateCartTotal(updatedCartItems)

                                _cartUiState.update {
                                    it.copy(
                                        cartItems = updatedCartItems,
                                        cart = updatedCart,
                                        cartTotal = cartTotal,
                                        totalSelectedItems = totalSelectedItems
                                    )
                                }
                                showToast("Item removed successfully")
                            }
                        }
                        is Result.Error -> {
                            val errorMessage = result.error.toString()
                            _cartUiState.update { it.copy(errorMessage = errorMessage) }
                            showToast(errorMessage)

                        }
                    }
                } else {
                    val errorMessage = "Item not found in the cart"
                    _cartUiState.update { it.copy(errorMessage = errorMessage) }
                    showToast(errorMessage)
                }
            } catch (e: Exception) {
                _cartUiState.update { it.copy(errorMessage = e.message) }
            } finally {
                _cartUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateExistingItemQuantity(
        cartItems: List<CartItem>,
        productId: String,
        quantity: Int
    ): List<CartItem> {
        return cartItems.map {
            if (it.productId == productId && it.product != null) {
                val finalQuantity = it.quantity + quantity
                it.copy(
                    quantity = finalQuantity,
                    price = it.product!!.discountedPrice,
                    totalPrice = finalQuantity * it.product!!.discountedPrice
                )
            } else it
        }
    }


    fun getUserCart() {
        viewModelScope.launch {
            _cartUiState.update { it.copy(errorMessage = null, isLoading = true) }
            try {
                when (val cartResult = cartRepository.getUserCart()) {
                    is Result.Success -> {
                        val cart = cartResult.data
                        when (val itemResult = cartRepository.getCartItems()) {
                            is Result.Success -> {
                                val cartItems = itemResult.data
                                var totalSelectedItems = cartItems.count { it.isSelected == true }
                                val cartTotal = calculateCartTotal(cartItems)
                                Log.i("cartItems", cartItems.toString())
                                _cartUiState.update {
                                    it.copy(
                                        cart = cart,
                                        cartItems = cartItems,
                                        totalSelectedItems = totalSelectedItems,
                                        cartTotal = cartTotal
                                    )
                                }
                            }

                            is Result.Error -> {
                                _cartUiState.update {
                                    it.copy(cart = cart, errorMessage = itemResult.error.toString())
                                }
                            }
                        }
                    }

                    is Result.Error -> {
                        _cartUiState.update {
                            it.copy(errorMessage = cartResult.error.toString())
                        }
                    }
                }

            } catch (e: Exception) {
                _cartUiState.update { it.copy(errorMessage = e.message) }
            } finally {
                _cartUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateItemSelection(cartItemId: String, isSelected: Boolean) {
        viewModelScope.launch {
            _cartUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                when (val result = cartRepository.updateItemSelection(cartItemId, isSelected)) {
                    is Result.Success -> {
                        val updatedItems = cartUiState.value.cartItems?.map {
                            if (it.id == cartItemId) it.copy(isSelected = isSelected) else it
                        } ?: emptyList()

                        val totalSelectedItems = updatedItems.count { it.isSelected == true }
                        val cartTotal = calculateCartTotal(updatedItems)

                        _cartUiState.update {
                            it.copy(
                                cartItems = updatedItems,
                                totalSelectedItems = totalSelectedItems,
                                cartTotal = cartTotal
                            )
                        }
                    }

                    is Result.Error -> _cartUiState.update {
                        it.copy(errorMessage = result.error.toString())
                    }
                }
            } catch (e: Exception) {
                _cartUiState.update { it.copy(errorMessage = e.message) }
            } finally {
                _cartUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun calculateCartTotal(cartItems: List<CartItem>): Double {
        return cartItems
            .filter { it.isSelected }
            .sumOf { it.totalPrice }
    }

    private fun observeOrderEvents() {
        viewModelScope.launch {
            orderEventManager.events.collect { event ->
                if (event is OrderEvent.orderPlaced) {
                    _cartUiState.update { CartUiState() }
                    getUserCart()
                }
            }
        }
    }

    private fun observeAuthEvents() {
        viewModelScope.launch {
            authEventManager.events.collect { event ->
                if (event is Event.LoggedIn) {
                    _cartUiState.update { CartUiState() }
                    getUserCart()
                }
            }
        }
    }
}
