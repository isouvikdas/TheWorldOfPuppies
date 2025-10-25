package com.example.theworldofpuppies.membership.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.core.domain.PayingFor
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.membership.domain.PremiumOption
import com.example.theworldofpuppies.membership.domain.PremiumOptionRepository
import com.example.theworldofpuppies.membership.domain.PremiumOptionUiState
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.order.data.requests.PaymentVerificationRequest
import com.example.theworldofpuppies.shop.order.domain.PaymentMethod
import com.razorpay.Checkout
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

class PremiumOptionViewModel(
    private val premiumOptionRepository: PremiumOptionRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _premiumOptionUiState = MutableStateFlow(PremiumOptionUiState())
    val premiumOptionUiState = _premiumOptionUiState.asStateFlow()

    private val _payingForEvent = MutableSharedFlow<PayingFor>()
    val payingForEvent: SharedFlow<PayingFor> = _payingForEvent.asSharedFlow()

    init {
        val id = userRepository.getMembershipId()
        if (userRepository.isLoggedIn() && id != null) {
            getPremiumOptionOrder(id)
        }
    }

    fun onAddressChangeClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun onJoinNowClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.MembershipCheckoutScreen.route)
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _premiumOptionUiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun selectOption(option: PremiumOption) {
        viewModelScope.launch {
            _premiumOptionUiState.update { it.copy(selectedOption = option) }
        }
    }

    fun forceLoad() {
        viewModelScope.launch {
            _premiumOptionUiState.update { it.copy(isRefreshing = true) }
            getPremium()
            val orderId = userRepository.getMembershipId()
            if (orderId != null) {
                getPremiumOptionOrder(orderId)
            }
            delay(1500)
            _premiumOptionUiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun getPremium() {
        viewModelScope.launch {
            try {
                _premiumOptionUiState.update { it.copy(isLoading = true, errorMessage = null) }
                when (val result = premiumOptionRepository.getPremiumOptions()) {
                    is Result.Success -> {
                        _premiumOptionUiState.update { it.copy(premiumOptions = result.data) }
                    }

                    is Result.Error -> {
                        _premiumOptionUiState.update { it.copy(errorMessage = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("premium error", e.message.toString())
                _premiumOptionUiState.update { it.copy(errorMessage = NetworkError.UNKNOWN) }

            } finally {
                _premiumOptionUiState.update { it.copy(isLoading = false, isAlreadyLoaded = true) }
            }
        }
    }

    fun buyPremium(premiumOptionId: String, context: Context) {
        viewModelScope.launch {
            try {
                _premiumOptionUiState.update { it.copy(checkoutLoading = true, errorMessage = null) }
                when (val result = premiumOptionRepository.buyPremium(premiumOptionId)) {
                    is Result.Success -> {
                        val premiumOptionOrder = result.data
                        _premiumOptionUiState.update {
                            it.copy(
                                premiumOptionOrder = premiumOptionOrder
                            )
                        }
                        startRazorpayCheckout(
                            keyId = premiumOptionOrder.keyId,
                            razorpayOrderId = premiumOptionOrder.razorpayOrderId,
                            amount = premiumOptionOrder.amount,
                            name = userRepository.getUserName() ?: "",
                            contact = userRepository.getUserPhoneNumber() ?: "",
                            context = context,
                            email = userRepository.getUserEmail() ?: ""
                        )
                        _payingForEvent.emit(PayingFor.MEMBERSHIP)
                    }

                    is Result.Error -> {
                        _premiumOptionUiState.update {
                            it.copy(errorMessage = result.error)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("premium buy", e.message.toString())
                _premiumOptionUiState.update { it.copy(errorMessage = NetworkError.UNKNOWN) }

            } finally {
                _premiumOptionUiState.update { it.copy(checkoutLoading = false) }
            }
        }
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


    fun verifyPayment(
        razorpayOrderId: String,
        signature: String,
        paymentId: String,
        navController: NavController
    ) {
        viewModelScope.launch {

            try {
                _premiumOptionUiState.update { it.copy(checkoutLoading = true) }
                lateinit var orderId: String
                lateinit var userId: String
                lateinit var publicOrderId: String

                val premiumOptionOrder = premiumOptionUiState.value.premiumOptionOrder

                premiumOptionOrder?.id?.let {
                    orderId = it
                }

                premiumOptionOrder?.id?.let {
                    publicOrderId = it
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

                when (val result =
                    premiumOptionRepository.verifyPayment(paymentVerificationRequest)) {
                    is Result.Success -> {
                        userRepository.saveMembershipId(orderId)
                        navController.popBackStack()
                    }

                    is Result.Error -> {
                        _premiumOptionUiState.update { it.copy(errorMessage = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("payment verification", e.message.toString())
                _premiumOptionUiState.update { it.copy(errorMessage = NetworkError.UNKNOWN) }
            } finally {
                _premiumOptionUiState.update { it.copy(checkoutLoading = false) }
            }
        }
    }

    fun handlePaymentError(code: Int, message: String) {
        _premiumOptionUiState.update { it.copy(errorMessage = NetworkError.PAYMENT_VERIFICATION_FAILED) }
    }

    fun getPremiumOptionOrder(premiumOptionOrderId: String) {
        viewModelScope.launch {
            try {
                _premiumOptionUiState.update { it.copy(isLoading = true, errorMessage = null) }
                when (val result =
                    premiumOptionRepository.getPremiumOptionOrder(premiumOptionOrderId)) {
                    is Result.Success -> {
                        _premiumOptionUiState.update { it.copy(premiumOptionOrder = result.data) }
                    }

                    is Result.Error -> {
                        _premiumOptionUiState.update { it.copy(errorMessage = result.error) }
                    }
                }
            } catch (e: Exception) {
                Log.e("premium order", e.message.toString())
                _premiumOptionUiState.update { it.copy(errorMessage = NetworkError.UNKNOWN) }
            } finally {
                _premiumOptionUiState.update { it.copy(isLoading = false) }
            }
        }
    }

}