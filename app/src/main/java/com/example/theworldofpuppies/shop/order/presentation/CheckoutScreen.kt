package com.example.theworldofpuppies.shop.order.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.cart.presentation.CartViewModel
import com.example.theworldofpuppies.shop.order.domain.OrderUiState
import com.example.theworldofpuppies.shop.order.domain.PaymentMethod
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    orderViewModel: OrderViewModel,
    cartViewModel: CartViewModel
) {
    val cartUiState by cartViewModel.cartUiState.collectAsStateWithLifecycle()
    val orderUiState by orderViewModel.orderUiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val addresses = remember(orderUiState.addresses) { orderUiState.addresses }
    val selectedAddress = remember(orderUiState.selectedAddress) { orderUiState.selectedAddress }

    val selectedPaymentMethod by orderViewModel.selectedPaymentMethod.collectAsStateWithLifecycle()

    val shippingFee = 30.00
    val cartTotal = remember(cartUiState.cartTotal) { cartUiState.cartTotal }
    val total = cartTotal + shippingFee

    val orderId = orderUiState.orderId


    val context = LocalContext.current
    LaunchedEffect(Unit) {
        orderViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    if (orderUiState.showSuccessDialog && orderId != null) {

        OrderSuccessDialog(
            orderId = orderId,
            onViewOrder = {
                orderViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Home.route
                )

            },
            onContinueShopping = {
                orderViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Shop.route
                )
            },
            onDismiss = {
                orderViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Shop.route
                )

            }
        )

    }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            CheckoutHeader(scrollBehavior = scrollBehavior, navController = navController)
        },
        bottomBar = {
            CheckoutBottomSection(
                totalCartPrice = cartUiState.cartTotal,
                shippingFee = shippingFee,
                total = total,
                orderUiState = orderUiState,
                selectedAddress = selectedAddress,
                selectedPaymentMethod = selectedPaymentMethod,
                onClick = { orderViewModel.onPlaceOrderClick(context = context) }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.small1.div(4).times(5))
            ) {
                AddressSection(
                    modifier = Modifier
                        .padding(vertical = MaterialTheme.dimens.extraSmall),
                    addresses = addresses,
                    orderViewModel = orderViewModel,
                    selectedAddress = selectedAddress
                )

                OrderSection(
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.extraSmall.times(2)),
                    selectedPaymentMethod = selectedPaymentMethod,
                    orderViewModel = orderViewModel
                )
            }
        }
    }
}


@Composable
fun AddressSection(
    modifier: Modifier = Modifier,
    addresses: List<Address>,
    orderViewModel: OrderViewModel,
    selectedAddress: Address? = null
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Text(
            text = "Shipping to",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        addresses.forEach { address ->
            val isSelected = selectedAddress?.id == address.id
            val addressTitle = remember(address.addressType) { address.addressType }
            AddressCard(
                isSelected = isSelected,
                addressTitle = addressTitle.name,
                addressDescription = "${address.houseNumber} ${address.landmark} ${address.city} ${address.state} ${address.pinCode}",
                phoneNumber = address.contactNumber,
                orderViewModel = orderViewModel,
                address = address
            )
        }
    }
}

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    addressTitle: String,
    addressDescription: String,
    phoneNumber: String,
    orderViewModel: OrderViewModel,
    address: Address
) {
    Surface(
        modifier = modifier
            .wrapContentSize(),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isSelected) 8.dp else 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    if (isSelected) Color.White.copy(0.2f)
                    else MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                IconButton(
                    onClick = {
                        orderViewModel.updateAddressSelection(address)
                    },
                    modifier = Modifier.padding(end = MaterialTheme.dimens.extraSmall)
                ) {
                    Icon(
                        if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.dimens.small2 + MaterialTheme.dimens.extraSmall / 2),
                        tint = Color.Red
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.7f)
                    .padding(vertical = MaterialTheme.dimens.extraSmall),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = addressTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray.copy(0.7f),
                    modifier = Modifier,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = addressDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray.copy(0.7f),
                    modifier = Modifier,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(onClick = {}, modifier = Modifier.align(Alignment.TopEnd)) {
                    Icon(
                        painterResource(R.drawable.pencil),
                        contentDescription = "Edit Address icon",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

}


@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    selectedPaymentMethod: PaymentMethod?,
    orderViewModel: OrderViewModel
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Text(
            text = "Payment Methods",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            modifier = Modifier.wrapContentSize(),
            color = Color.White.copy(0.45f),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(start = 6.dp)) {
                Spacer(modifier = Modifier.height(5.dp))
                PaymentMethods(
                    method = "Pay Online",
                    methodStartPadding = MaterialTheme.dimens.small1,
                    methodDescription = "Pay using credit/debit cards, UPI, net banking and more",
                    image = R.drawable.master_card,
                    imageSize = 50.dp,
                    selected = selectedPaymentMethod is PaymentMethod.ONLINE,
                    onClick = { orderViewModel.updatePaymentMethodSelection(PaymentMethod.ONLINE) }
                )
                Spacer(modifier = Modifier.height(20.dp))
                PaymentMethods(
                    method = "Cash On Delivery",
                    methodStartPadding = MaterialTheme.dimens.small2,
                    methodDescription = "Pay using Cash on Delivery",
                    image = R.drawable.cod,
                    imageSize = 40.dp,
                    selected = selectedPaymentMethod is PaymentMethod.COD,
                    onClick = { orderViewModel.updatePaymentMethodSelection(PaymentMethod.COD) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun PaymentMethods(
    modifier: Modifier = Modifier,
    method: String,
    methodStartPadding: Dp,
    methodDescription: String,
    image: Int,
    imageSize: Dp,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painterResource(image),
                    contentDescription = null,
                    modifier = Modifier.size(imageSize)
                )
                Text(
                    text = method,
                    modifier = Modifier.padding(start = methodStartPadding),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.W500
                )
            }

            Text(
                methodDescription,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.medium2),
                color = Color.Black.copy(0.8f)
            )
        }

        IconButton(
            onClick = {
                onClick()
            }
        ) {
            Icon(
                if (selected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimens.small2 + MaterialTheme.dimens.extraSmall / 2),
                tint = Color.Red
            )
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {

    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth(),
        title = {},
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.dimens.small1)
                        .size(
                            MaterialTheme.dimens.small1 + MaterialTheme.dimens.extraSmall.times(
                                3
                            )
                        )
                ) {
                    Icon(
                        painterResource(R.drawable.arrow_left_filled),
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(21.dp)
                    )
                }
                Text(
                    text = "Checkout",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall)
                )
                IconButton(
                    onClick = { navController.navigate(Screen.CartScreen.route) },
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall)
                ) {
                    Icon(
                        painterResource(R.drawable.bag_outline),
                        contentDescription = "Bag",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

        }
    )

}

@Composable
fun CheckoutBottomSection(
    modifier: Modifier = Modifier,
    totalCartPrice: Double,
    shippingFee: Double,
    total: Double,
    onClick: () -> Unit,
    orderUiState: OrderUiState,
    selectedAddress: Address? = null,
    selectedPaymentMethod: PaymentMethod
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.extraLarge3)
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
                .fillMaxSize()
                .background(Color.LightGray.copy(0.55f))
                .padding(
                    start = MaterialTheme.dimens.small1.times(5).div(4),
                    end = MaterialTheme.dimens.small1.times(5).div(4),
                    top = MaterialTheme.dimens.small2,
                    bottom = MaterialTheme.dimens.small1
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            PriceSection(priceTitle = "Shipping fee", price = shippingFee)
            PriceSection(priceTitle = "Sub total", price = totalCartPrice)
            PriceSection(
                priceTitle = "Total",
                price = total,
                priceStyle = MaterialTheme.typography.headlineLarge,
                priceWeight = FontWeight.SemiBold,
                priceTitleStyle = MaterialTheme.typography.titleLarge,
                priceTitleWeight = FontWeight.SemiBold
            )

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
                if (orderUiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                } else {
                    Text(
                        text = "Place Order",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                }
            }
        }
    }
}

@Composable
fun PriceSection(
    modifier: Modifier = Modifier,
    priceTitle: String,
    price: Double,
    priceTitleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    priceTitleWeight: FontWeight = FontWeight.W500,
    priceStyle: TextStyle = MaterialTheme.typography.titleLarge,
    priceWeight: FontWeight = FontWeight.SemiBold
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = priceTitle,
            style = priceTitleStyle,
            fontWeight = priceTitleWeight
        )

        Text(
            text = formatCurrency(price),
            style = priceStyle,
            fontWeight = priceWeight
        )
    }
}
