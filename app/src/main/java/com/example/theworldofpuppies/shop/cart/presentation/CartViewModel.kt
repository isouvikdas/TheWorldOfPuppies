package com.example.theworldofpuppies.shop.cart.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.cart.domain.CartItem
import com.example.theworldofpuppies.shop.cart.domain.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState.asStateFlow()

    init {
        getUserCart()
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
            .filter { it.isSelected == true }
            .sumOf { it.totalPrice }
    }
}
