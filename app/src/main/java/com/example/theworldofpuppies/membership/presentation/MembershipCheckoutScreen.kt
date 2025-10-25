package com.example.theworldofpuppies.membership.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.membership.domain.PremiumOptionUiState
import com.example.theworldofpuppies.shop.order.domain.PaymentMethod
import com.example.theworldofpuppies.shop.order.presentation.AddressSection
import com.example.theworldofpuppies.shop.order.presentation.CheckoutHeader
import com.example.theworldofpuppies.shop.order.presentation.PaymentMethods
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipCheckoutScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addressViewModel: AddressViewModel,
    addressUiState: AddressUiState,
    premiumOptionViewModel: PremiumOptionViewModel,
    premiumOptionUiState: PremiumOptionUiState
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val context = LocalContext.current

    val selectedOption = premiumOptionUiState.selectedOption

    val selectedPaymentMethod = premiumOptionUiState.selectedPaymentMethod

    val selectedAddress = addressUiState.addresses.find { it.isSelected }
    val addressList = addressUiState.addresses

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                CheckoutHeader(
                    scrollBehavior = scrollBehavior,
                    navController = navController
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        AddressSection(
                            modifier = Modifier
                                .padding(vertical = MaterialTheme.dimens.extraSmall)
                                .padding(horizontal = MaterialTheme.dimens.small1),
                            selectedAddress = selectedAddress,
                            addressViewModel = addressViewModel,
                            onAddressChangeClick = {
                                premiumOptionViewModel.onAddressChangeClick(navController)
                            },
                            navController = navController,
                            heading = "Shipping Address",
                            addressList = addressList
                        )
                    }

                    item {
                        PaymentMethodSection(
                            modifier = Modifier
                                .padding(horizontal = MaterialTheme.dimens.small1)
                                .padding(top = 12.dp),
                            selectedPaymentMethod = selectedPaymentMethod,
                            premiumOptionViewModel = premiumOptionViewModel
                        )
                    }
                }

                MembershipCheckoutBottomSection(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    total = 200.00,
                    onClick = {
                        if (selectedOption != null && selectedAddress != null && selectedPaymentMethod != null) {
                            premiumOptionViewModel.buyPremium(selectedOption.id, context)
                        }
                    },
                    selectedAddress = selectedAddress,
                    selectedPaymentMethod = PaymentMethod.ONLINE,
                    isLoading = false
                )
            }
        }

        if (premiumOptionUiState.checkoutLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent.copy(0.2f))
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun PaymentMethodSection(
    modifier: Modifier = Modifier,
    selectedPaymentMethod: PaymentMethod?,
    premiumOptionViewModel: PremiumOptionViewModel
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Payment Methods",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            modifier = Modifier.wrapContentSize(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
        ) {
            Surface(color = Color.LightGray.copy(0.4f)) {
                Column() {
                    PaymentMethods(
                        method = "PAY ONLINE",
                        methodDescription = "Pay using credit/debit cards, UPI, net banking and more",
                        image = R.drawable.master_card,
                        imageSize = 50.dp,
                        selected = selectedPaymentMethod is PaymentMethod.ONLINE,
                        onClick = {
                            premiumOptionViewModel.selectPaymentMethod(PaymentMethod.ONLINE)
                        }
                    )
                }

            }
        }
    }
}


@Composable
fun MembershipCheckoutBottomSection(
    modifier: Modifier = Modifier,
    total: Double,
    onClick: () -> Unit,
    selectedAddress: Address? = null,
    selectedPaymentMethod: PaymentMethod,
    isLoading: Boolean
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = MaterialTheme.dimens.small3,
                    topEnd = MaterialTheme.dimens.small3
                )
            ),
        color = Color.White,
        shadowElevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(0.55f))
                .padding(
                    start = MaterialTheme.dimens.small1.times(5).div(4),
                    end = MaterialTheme.dimens.small1.times(5).div(4),
                    top = MaterialTheme.dimens.small2,
                    bottom = MaterialTheme.dimens.small1
                ),
        ) {

            Button(
                onClick = {
                    onClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.buttonHeight),
                shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                } else {
                    Text(
                        text = "Place Order",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                }
            }
        }
    }
}
