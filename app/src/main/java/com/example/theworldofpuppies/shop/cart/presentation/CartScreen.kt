package com.example.theworldofpuppies.shop.cart.presentation

import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.shop.cart.domain.CartItem
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    cartUiState: CartUiState,
    cartViewModel: CartViewModel? = null
) {
    val cartItems = remember(cartUiState.cartItems) { cartUiState.cartItems as List<CartItem> }
    val lazyListSTate = rememberLazyListState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        cartViewModel?.toastEvent?.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            CartHeader(onBack = {
                onBack()
            }, scrollBehavior = scrollBehavior)
        },
        bottomBar = {
            CartBottomSection(
                totalCartPrice = cartUiState.cartTotal,
                totalSelectedItems = cartUiState.totalSelectedItems
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.Transparent
        ) {
            when {
                cartItems.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            MaterialTheme.dimens.small1.div(4).times(5)
                        ),
                        state = lazyListSTate
                    ) {
                        items(cartItems, key = { it.id }) { cartItem ->
                            val cartItemId = remember(cartItem.id) { cartItem.id }
                            val isSelected = remember(cartItem.isSelected) { cartItem.isSelected }
                            val productName =
                                remember(cartItem.product?.name) { cartItem.product?.name }
                            val totalPrice = remember(cartItem.totalPrice) { cartItem.totalPrice }
                            val quantity = remember(cartItem.quantity) { cartItem.quantity }
                            val productId = remember(cartItem.productId) { cartItem.productId }
                            CartItemSection(
                                cartViewModel = cartViewModel,
                                cartItemId = cartItemId,
                                isSelected = isSelected,
                                productName = productName,
                                totalPrice = totalPrice,
                                productId = productId,
                                quantity = quantity
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartHeader(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
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
                        onBack()
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
                    text = "My Cart",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall)
                )
                IconButton(
                    onClick = { /* Bag action */ },
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
fun CartItemSection(
    cartViewModel: CartViewModel? = null,
    cartItemId: String,
    isSelected: Boolean,
    productName: String?,
    totalPrice: Double,
    productId: String,
    quantity: Int
) {
    val base64Image = ""
    val byteArray = Base64.decode(base64Image, Base64.DEFAULT)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium2 + MaterialTheme.dimens.extraSmall)
            .padding(horizontal = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1.div(4))
            .padding(top = MaterialTheme.dimens.small1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*Cart selector button*/
        IconButton(
            onClick = {
                cartViewModel?.updateItemSelection(
                    cartItemId = cartItemId,
                    isSelected = !isSelected
                )
            },
            modifier = Modifier.padding(end = MaterialTheme.dimens.extraSmall.times(2))
        ) {
            Icon(
                if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimens.small2 + MaterialTheme.dimens.extraSmall / 2),
                tint = Color.Red
            )
        }
        /*Cart image Section*/
        Surface(
            modifier = Modifier
                .size(MaterialTheme.dimens.medium2 + MaterialTheme.dimens.extraSmall),
            color = Color.LightGray.copy(0.2f),
            shape = RoundedCornerShape(MaterialTheme.dimens.small1)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(byteArray)
                    .crossfade(true)
                    .error(R.drawable.login)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        /*Cart name and price section*/
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f)
                .padding(
                    horizontal = MaterialTheme.dimens.extraSmall.times(4),
                    vertical = MaterialTheme.dimens.extraSmall.div(3)
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = productName ?: "",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatCurrency(totalPrice),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W500,
                color = Color.Gray
            )
        }
        /*Cart quantity section*/
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = "Delete cart",
                tint = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier
                    .padding(end = MaterialTheme.dimens.extraSmall)
                    .size(MaterialTheme.dimens.small2)
                    .clickable { cartViewModel?.removeCartItem(productId) }
            )
            CartQuantitySection(
                modifier = Modifier.fillMaxWidth(),
                quantity = quantity,
                increaseQuantity = {
                    cartViewModel?.addToCart(
                        productId = productId.toString(),
                        1, isItProductScreen = false
                    )
                },
                decreaseQuantity = {
                    if (quantity > 1)
                        cartViewModel?.addToCart(
                            productId = productId.toString(),
                            -1, isItProductScreen = false
                        ) else cartViewModel?.removeCartItem(productId)
                }
            )
        }
    }

}

@Composable
fun CartQuantitySection(
    modifier: Modifier = Modifier,
    quantity: Int?,
    increaseQuantity: () -> Unit,
    decreaseQuantity: () -> Unit
) {
    val quantity = remember(quantity) { quantity }
    val formattedQuantity = quantity.toString().padStart(2, '0')
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.small1.times(3) / 2)
                .clip(CircleShape)
                .clickable {
                    decreaseQuantity()
                }
                .background(Color.White.copy(0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Remove,
                contentDescription = null,
                modifier = Modifier
                    .size(MaterialTheme.dimens.small1.times(5) / 4),
                tint = Color.Black
            )

        }
        Text(
            text = formattedQuantity,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.small1.times(3) / 2)
                .clip(CircleShape)
                .clickable {
                    increaseQuantity()
                }
                .background(Color.White.copy(0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(MaterialTheme.dimens.small1.times(5) / 4)
            )

        }
    }

}


@Composable
fun CartBottomSection(
    modifier: Modifier = Modifier,
    totalSelectedItems: Int,
    totalCartPrice: Double
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.extraLarge1)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Text(
                        text = "Selected Items",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = "(${totalSelectedItems})",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.W500
                    )

                }

                Row(
                    modifier = Modifier
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = formatCurrency(totalCartPrice),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }

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
                    text = "Checkout",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
