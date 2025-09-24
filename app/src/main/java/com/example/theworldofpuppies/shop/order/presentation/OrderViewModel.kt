package com.example.theworldofpuppies.shop.order.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.Event
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.order.data.requests.PaymentRequest
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest
import com.example.theworldofpuppies.shop.order.domain.OrderHistoryUiState
import com.example.theworldofpuppies.shop.order.domain.OrderRepository
import com.example.theworldofpuppies.shop.order.domain.OrderUiState
import com.example.theworldofpuppies.shop.order.domain.PaymentMethod
import com.example.theworldofpuppies.shop.order.domain.PaymentRepository
import com.example.theworldofpuppies.shop.order.domain.PaymentUiState
import com.example.theworldofpuppies.shop.order.presentation.utils.OrderEvent
import com.example.theworldofpuppies.shop.order.presentation.utils.OrderEventManager
import com.razorpay.Checkout
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val orderEventManager: OrderEventManager,
    private val authEventManager: AuthEventManager
) : ViewModel() {

    private val _orderUiState = MutableStateFlow(OrderUiState())
    val orderUiState: StateFlow<OrderUiState> = _orderUiState.asStateFlow()

    private val _orderHistoryUiState = MutableStateFlow(OrderHistoryUiState())
    val orderHistoryUiState: StateFlow<OrderHistoryUiState> = _orderHistoryUiState.asStateFlow()

    private val _paymentUiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow<PaymentMethod>(PaymentMethod.IDLE)
    val selectedPaymentMethod = _selectedPaymentMethod.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    init {
        if (!userRepository.getUserId().isNullOrEmpty()) {
            getCharges()
            getOrders()
        }
        observeAuthEvents()
        observeOrderEvents()
    }

    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun updatePaymentMethodSelection(paymentMethod: PaymentMethod) {
        _selectedPaymentMethod.value = paymentMethod
    }

    fun onAddressChangeClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun getCharges() {
        viewModelScope.launch {
            try {
                _orderUiState.update { it.copy(isLoading = true) }
                when (val result = orderRepository.getCharges()) {
                    is Result.Success -> {
                        _orderUiState.update { it.copy(shippingFee = result.data.shippingFee) }
                    }
                    is Result.Error -> {
                        _orderUiState.update { it.copy(error = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("orders fetching error", e.toString())
                _orderUiState.update { it.copy(error = NetworkError.UNKNOWN) }
            } finally {
                _orderUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onPlaceOrderClick(context: Context, selectedAddress: Address? = null) {
        viewModelScope.launch {
            if (selectedAddress == null) {
                showToast("Please select an address")
            } else if (selectedPaymentMethod.value == PaymentMethod.IDLE) {
                showToast("Please select a payment method")
            } else {
                when (selectedPaymentMethod.value) {
                    PaymentMethod.COD -> createCodOrder()
                    PaymentMethod.ONLINE -> createOrderAndStartPayment(context)
                    else -> {}
                }
            }

        }
    }

    fun getOrders() {
        viewModelScope.launch {
            try {
                _orderHistoryUiState.update { it.copy(isLoading = true, error = null) }
                when (val result = orderRepository.getOrders()) {
                    is Result.Success -> {
                        _orderHistoryUiState.update {
                            it.copy(orderHistory = result.data.toMutableList())
                        }
                    }

                    is Result.Error -> {
                        _orderHistoryUiState.update { it.copy(error = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("orders fetching error", e.toString())
                _orderHistoryUiState.update { it.copy(error = NetworkError.UNKNOWN) }
            } finally {
                _orderHistoryUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun getOrderById(orderId: String) {
        viewModelScope.launch {
            try {
                _orderHistoryUiState.update { it.copy(isLoading = true, error = null) }
                when (val result = orderRepository.getOrderById(orderId)) {
                    is Result.Success -> {
                        val order = result.data
                        val orders =
                            _orderHistoryUiState.value.orderHistory.map { if (order.id == it.id) order else it } as MutableList
                        _orderHistoryUiState.update {
                            it.copy(orderHistory = orders)
                        }
                    }

                    is Result.Error -> {
                        _orderHistoryUiState.update { it.copy(error = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("orders fetching error", e.toString())
                _orderHistoryUiState.update { it.copy(error = NetworkError.UNKNOWN) }
            } finally {
                _orderHistoryUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun createCodOrder() {
        viewModelScope.launch {
            try {
                _orderUiState.update { it.copy(isLoading = true, error = null) }
                when (val result = orderRepository.createCodOrder()) {
                    is Result.Success -> {
                        _orderUiState.update {
                            it.copy(
                                orderId = result.data.id,
                                publicOrderId = result.data.publicOrderId,
                                showSuccessDialog = true
                            )
                        }
                        orderEventManager.sendEvent(OrderEvent.OrderConfirmed(result.data.id))
                    }

                    is Result.Error -> {
                        _orderUiState.update { it.copy(error = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("cod error", e.toString())
                _orderUiState.update { it.copy(error = NetworkError.UNKNOWN) }
            } finally {
                _orderUiState.update { it.copy(isLoading = false) }
            }
        }
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
                            publicOrderId = order.publicOrderId,
                            userId = order.userId
                        )
                        when (val paymentResult =
                            paymentRepository.createPaymentOrder(paymentRequest)) {
                            is Result.Success -> {
                                val paymentResponse = paymentResult.data
                                _orderUiState.update {
                                    it.copy(
                                        orderId = paymentResponse.orderId,
                                        publicOrderId = order.publicOrderId,
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
                                    context = context,
                                    email = userRepository.getUserEmail() ?: ""
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
            lateinit var publicOrderId: String

            orderUiState.value.paymentResponse?.let {
                orderId = it.orderId
            }

            orderUiState.value.paymentResponse?.let {
                publicOrderId = it.publicOrderId
            }
            userRepository.getUserId()?.let {
                userId = it
            }

            val paymentVerificationRequest = PaymentVerificationRequest(
                razorpayOrderId = razorpayOrderId,
                orderId = orderId,
                paymentId = paymentId,
                signature = signature,
                userId = userId,
                publicOrderId = publicOrderId
            )
            when (paymentRepository.verifyPaymentOrder(paymentVerificationRequest)) {
                is Result.Success -> {
                    _paymentUiState.value =
                        PaymentUiState.Success("Payment Verification Successful")
                    orderEventManager.sendEvent(OrderEvent.OrderConfirmed(orderId))
                    _orderUiState.update { it.copy(showSuccessDialog = true) }

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
        context: Context,
        email: String
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
                put("email", email)
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

    fun dismissDialog(
        navController: NavController,
        route: String,
        popUpToRoute: String = Screen.ProductListScreen.route,
        navigationEnabled: Boolean = true
    ) {
        _orderUiState.update { it.copy(showSuccessDialog = false) }
        if (navigationEnabled) {
            navController.navigate(route) {
                popUpTo(popUpToRoute) { inclusive = false }
                launchSingleTop = true
            }
        }
    }


    private fun observeAuthEvents() {
        viewModelScope.launch {
            authEventManager.events.collect { event ->
                if (event is Event.LoggedIn) {
                    _orderHistoryUiState.update { OrderHistoryUiState() }
                    getOrders()
                }
            }
        }
    }

    private fun observeOrderEvents() {
        viewModelScope.launch {
            orderEventManager.events.collect { event ->
                if (event is OrderEvent.OrderConfirmed) {
                    getOrders()
                }
            }
        }
    }

}