package com.example.theworldofpuppies.shop.order.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressRepository
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.shop.order.data.requests.PaymentRequest
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest
import com.example.theworldofpuppies.shop.order.domain.OrderRepository
import com.example.theworldofpuppies.shop.order.domain.OrderUiState
import com.example.theworldofpuppies.shop.order.domain.PaymentRepository
import com.example.theworldofpuppies.shop.order.domain.PaymentUiState
import com.razorpay.Checkout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _orderUiState = MutableStateFlow(OrderUiState())
    val orderUiState: StateFlow<OrderUiState> = _orderUiState.asStateFlow()

    private val _paymentUiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()

    init {
        getAddresses()
    }

    fun getAddresses() {
        viewModelScope.launch {
            try {
                when (val addressResult = addressRepository.getAddresses()) {
                    is Result.Success -> {
                        _orderUiState.update { it.copy(addresses = addressResult.data as MutableList<Address>) }
                    }

                    is Result.Error -> {
                        _orderUiState.update { it.copy(error = NetworkError.SERVER_ERROR) }
                    }
                }
            } catch (e: Exception) {
                _orderUiState.update { it.copy(error = NetworkError.UNKNOWN) }
            }
        }
    }

    fun selectUnselectPod() {
        _orderUiState.update { it.copy(isPodSelected = !it.isPodSelected) }
    }

    fun updateAddressSelection(address: Address) {
        _orderUiState.update { it.copy(selectedAddress = address) }
    }

    fun createOrderAndStartPayment(context: Context) {
        viewModelScope.launch {
            try {
                _orderUiState.update { it.copy(isLoading = true) }
                Log.i("order", orderUiState.value.isLoading.toString())
                when (val result = orderRepository.createOrder()) {
                    is Result.Success -> {
                        val order = result.data
                        val paymentRequest = PaymentRequest(
                            price = order.totalAmount,
                            orderId = order.id,
                            userId = order.userId
                        )
                        when (val paymentResult =
                            paymentRepository.createPaymentOrder(paymentRequest)) {
                            is Result.Success -> {
                                val paymentResponse = paymentResult.data
                                _orderUiState.update {
                                    it.copy(
                                        paymentResponse = paymentResponse,
                                        isLoading = false
                                    )
                                }
                                startRazorpayCheckout(
                                    keyId = paymentResponse.keyId,
                                    razorpayOrderId = paymentResponse.razorpayOrderId,
                                    amount = paymentResponse.price,
                                    name = userRepository.getUserName() ?: "",
                                    contact = userRepository.getUserPhoneNumber() ?: "",
                                    context = context
                                )
                            }

                            is Result.Error -> {
                                _paymentUiState.value = PaymentUiState.Error("Payment Failed")
                            }

                        }
                    }

                    is Result.Error -> {
                        _orderUiState.update { it.copy(error = result.error) }
                    }
                }


            } catch (e: Exception) {
                _orderUiState.update { it.copy(error = NetworkError.UNKNOWN) }
                Log.e("payment", e.message ?: "unknown error")
            } finally {
                _orderUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun verifyPayment(
        razorpayOrderId: String,
        signature: String,
        paymentId: String,
    ) {
        viewModelScope.launch {
            _paymentUiState.value = PaymentUiState.Loading

            lateinit var orderId: String
            lateinit var userId: String

            orderUiState.value.paymentResponse?.let {
                orderId = it.orderId
            }
            userRepository.getUserId()?.let {
                userId = it
            }

            val paymentVerificationRequest = PaymentVerificationRequest(
                razorpayOrderId = razorpayOrderId,
                orderId = orderId,
                paymentId = paymentId,
                signature = signature,
                userId = userId
            )
            when (paymentRepository.verifyPaymentOrder(paymentVerificationRequest)) {
                is Result.Success -> {
                    _paymentUiState.value =
                        PaymentUiState.Success("Payment Verification Successful")
                }

                is Result.Error -> {
                    _paymentUiState.value =
                        PaymentUiState.Error(R.string.payment_verification_failed.toString())
                }
            }
        }
    }

    fun handlePaymentError(code: Int, message: String) {
        _paymentUiState.value = PaymentUiState.Error(message)
    }

    private fun startRazorpayCheckout(
        keyId: String,
        razorpayOrderId: String,
        amount: Int,
        name: String,
        contact: String,
        context: Context
    ) {
        val activity = context as Activity
        val checkout = Checkout()
        checkout.setKeyID(keyId)

        val options = JSONObject().apply {
            put("name", name)
            put("description", "Order Payment")
            put("currency", "INR")
            put("amount", amount)
            put("order_id", razorpayOrderId)

            put("prefill", JSONObject().apply {
                put("contact", contact)
            })
            put("method", JSONObject().apply {
                put("upi", true)
                put("qr", true)
            })
            put("upi", JSONObject().apply {
                put("flow", "intent")
            })
        }

        checkout.open(activity, options)
    }

}