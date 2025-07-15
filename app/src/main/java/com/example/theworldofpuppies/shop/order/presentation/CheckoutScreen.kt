package com.example.theworldofpuppies.shop.order.presentation

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val isSelected = rememberSaveable { mutableStateOf(true) }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            CheckoutHeader(scrollBehavior = scrollBehavior, navController = navController)
        },
        bottomBar = {
            CheckoutBottomSection(
                totalCartPrice = 100.00,
                shippingFee = 30.00,
                total = 130.00
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
                    isSelected = isSelected, modifier = Modifier
                        .padding(vertical = MaterialTheme.dimens.extraSmall)
                )

                PaymentSection(
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.extraSmall.times(2))
                )

            }
        }
    }

}

@Composable
fun PaymentSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Text(
            text = "Payment Method",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            modifier = Modifier.wrapContentSize(),
            color = Color.White.copy(0.45f),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(start = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isSelected = rememberSaveable { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.7f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painterResource(R.drawable.master_card),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Text(
                            text = "Credit/Debit Card",
                            modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.W500
                        )
                    }

                    IconButton(
                        onClick = {
                            isSelected.value = !isSelected.value
                        }
                    ) {
                        Icon(
                            if (isSelected.value) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.small2 + MaterialTheme.dimens.extraSmall / 2),
                            tint = Color.Red
                        )
                    }

                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isSelected = rememberSaveable { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.7f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painterResource(R.drawable.upi_color),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Text(
                            text = "UPI",
                            modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.W500
                        )
                    }

                    IconButton(
                        onClick = {
                            isSelected.value = !isSelected.value
                        }
                    ) {
                        Icon(
                            if (isSelected.value) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.small2 + MaterialTheme.dimens.extraSmall / 2),
                            tint = Color.Red
                        )
                    }

                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isSelected = rememberSaveable { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.7f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painterResource(R.drawable.cod),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Cash on Delivery",
                            modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.W500
                        )
                    }

                    IconButton(
                        onClick = {
                            isSelected.value = !isSelected.value
                        }
                    ) {
                        Icon(
                            if (isSelected.value) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.small2 + MaterialTheme.dimens.extraSmall / 2),
                            tint = Color.Red
                        )
                    }

                }

            }
        }

    }
}

@Composable
fun AddressSection(modifier: Modifier = Modifier, isSelected: MutableState<Boolean>) {
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

        AddressCard(
            isSelected = isSelected,
            addressTitle = "Home",
            address = "Kolkata, WestBengal, India, Asia, Earth",
            phoneNumber = "+91 60091 81866"
        )
        AddressCard(
            isSelected = isSelected,
            addressTitle = "Office",
            address = "Kolkata, WestBengal, India, Asia, Earth",
            phoneNumber = "+91 97745 98137"
        )

    }
}

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    isSelected: MutableState<Boolean>,
    addressTitle: String,
    address: String,
    phoneNumber: String
) {
    Surface(
        modifier = modifier
            .wrapContentSize(),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isSelected.value) 8.dp else 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    if (isSelected.value) Color.White.copy(0.2f)
                    else MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                IconButton(
                    onClick = {
                        isSelected.value = !isSelected.value
                    },
                    modifier = Modifier.padding(end = MaterialTheme.dimens.extraSmall)
                ) {
                    Icon(
                        if (isSelected.value) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
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
                    text = address,
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
                        .padding(horizontal = MaterialTheme.dimens.extraSmall)
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
    total: Double
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
                onClick = {},
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
                Text(
                    text = "Payment",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
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
