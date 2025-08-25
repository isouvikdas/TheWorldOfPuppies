package com.example.theworldofpuppies.shop.order.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.AddressCard
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
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
    cartViewModel: CartViewModel,
    addressViewModel: AddressViewModel,
    addressUiState: AddressUiState,
    orderUiState: OrderUiState
) {
    val cartUiState by cartViewModel.cartUiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val selectedAddress = addressUiState.addresses.find { it.isSelected }
    val addressList = addressUiState.addresses

    val selectedPaymentMethod by orderViewModel.selectedPaymentMethod.collectAsStateWithLifecycle()

    val shippingFee = orderUiState.shippingFee ?: 0.0
    val cartTotal = remember(cartUiState.cartTotal) { cartUiState.cartTotal }
    val total = cartTotal + shippingFee

    val publicOrderId = orderUiState.publicOrderId


    val context = LocalContext.current
    LaunchedEffect(Unit) {
        orderViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    if (orderUiState.showSuccessDialog && publicOrderId != null) {

        OrderSuccessDialog(
            orderId = publicOrderId,
            onViewOrder = {
                orderViewModel.dismissDialog(
                    navController = navController,
                    route = Screen.OrderHistoryScreen.route,
                    popUpToRoute = BottomNavigationItems.Shop.route,
                )

            },
            onContinueShopping = {
                orderViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Shop.route,
                    popUpToRoute = BottomNavigationItems.Shop.route,
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
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            CheckoutHeader(scrollBehavior = scrollBehavior, navController = navController)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Color.Transparent
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        AddressSection(
                            modifier = Modifier
                                .padding(vertical = MaterialTheme.dimens.extraSmall)
                                .padding(horizontal = MaterialTheme.dimens.small1),
                            selectedAddress = selectedAddress,
                            addressViewModel = addressViewModel,
                            onAddressChangeClick = {
                                orderViewModel.onAddressChangeClick(navController)
                            },
                            navController = navController,
                            heading = "Shipping Address",
                            addressList = addressList
                        )
                    }

                    item {
                        DeliverySection(modifier = Modifier.padding(vertical = MaterialTheme.dimens.extraSmall))
                    }

                    item {
                        OrderSection(
                            modifier = Modifier
                                .padding(top = MaterialTheme.dimens.extraSmall.times(2))
                                .padding(horizontal = MaterialTheme.dimens.small1),
                            selectedPaymentMethod = selectedPaymentMethod,
                            orderViewModel = orderViewModel
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large3.times(2)))
                    }
                }

                CheckoutBottomSection(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    totalCartPrice = cartUiState.cartTotal,
                    shippingFee = shippingFee,
                    total = total,
                    orderUiState = orderUiState,
                    selectedAddress = selectedAddress,
                    selectedPaymentMethod = selectedPaymentMethod,
                    onClick = {
                        orderViewModel.onPlaceOrderClick(
                            context = context,
                            selectedAddress = selectedAddress
                        )
                    }
                )

            }
        }
    }
}


@Composable
fun AddressSection(
    modifier: Modifier = Modifier,
    selectedAddress: Address?,
    addressViewModel: AddressViewModel,
    onAddressChangeClick: () -> Unit,
    navController: NavController,
    heading: String,
    addressList: List<Address>? = null
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = heading,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (!addressList.isNullOrEmpty()) {
                Text(
                    text = "Change",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .bounceClick {
                            onAddressChangeClick()
                        }
                )
            }
        }
        if (selectedAddress != null && !addressList.isNullOrEmpty()) {
            AddressCard(
                address = selectedAddress,
                addressViewModel = addressViewModel,
                isCheckoutScreen = true,
                navController = navController
            )
        }

        if (addressList.isNullOrEmpty()) {
            Surface(
                modifier = Modifier.wrapContentSize(),
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Surface(color = Color.LightGray.copy(0.4f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 50.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "You haven't added any address yet",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W500
                        )
                        FloatingActionButton(
                            onClick = {
                                addressViewModel.onAddAddressClick(navController)
                            },
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun DeliverySection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Text(
            text = "Delivery Details",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
        ) {
            Surface(
                color = Color.LightGray.copy(0.4f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.small1),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Standard",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.W500,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "2-5 Days",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray
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
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            modifier = Modifier.wrapContentSize(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
        ) {
            Surface(color = Color.LightGray.copy(0.4f)) {
                Column(modifier = Modifier.padding(start = 6.dp)) {
                    PaymentMethods(
                        method = "PAY ONLINE",
                        methodDescription = "Pay using credit/debit cards, UPI, net banking and more",
                        image = R.drawable.master_card,
                        imageSize = 50.dp,
                        selected = selectedPaymentMethod is PaymentMethod.ONLINE,
                        onClick = { orderViewModel.updatePaymentMethodSelection(PaymentMethod.ONLINE) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                        thickness = 0.15.dp
                    )
                    PaymentMethods(
                        method = "CASH ON DELIVERY",
                        methodDescription = "Pay using Cash on Delivery",
                        image = R.drawable.cash,
                        imageSize = 40.dp,
                        selected = selectedPaymentMethod is PaymentMethod.COD,
                        onClick = { orderViewModel.updatePaymentMethodSelection(PaymentMethod.COD) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

            }
        }
    }
}

@Composable
fun PaymentMethods(
    modifier: Modifier = Modifier,
    method: String,
    methodDescription: String,
    image: Int,
    imageSize: Dp,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = MaterialTheme.dimens.small1),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.width(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(image),
                    contentDescription = null,
                    modifier = Modifier.size(imageSize)
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = method,
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W500
                )
                Text(
                    methodDescription,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
//                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
//used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Checkout"
    ) {

        Icon(
            painterResource(R.drawable.bag_outline),
            contentDescription = "Cart",
            modifier = Modifier
                .size(21.dp)
                .bounceClick {
                    navController.navigate(Screen.CartScreen.route)
                }
        )
    }
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
