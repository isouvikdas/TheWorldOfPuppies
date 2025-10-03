package com.example.theworldofpuppies.shop.order.presentation.order_history

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.core.presentation.util.formatEpochMillis
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.review.domain.ReviewUiState
import com.example.theworldofpuppies.review.presentation.RatingCard
import com.example.theworldofpuppies.review.presentation.ReviewViewModel
import com.example.theworldofpuppies.shop.order.domain.Order
import com.example.theworldofpuppies.shop.order.domain.OrderHistoryUiState
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    orderHistoryUiState: OrderHistoryUiState,
    reviewViewModel: ReviewViewModel,
    reviewUiState: ReviewUiState
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val orders = orderHistoryUiState.orderHistory

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        reviewViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                OrderHistoryHeader(scrollBehavior = scrollBehavior, navController = navController)
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = Color.Transparent
            ) {

                if (!orderHistoryUiState.isLoading && orders.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painterResource(R.drawable.dog_sad),
                            contentDescription = "dog",
                            modifier = Modifier.size(60.dp)
                        )
                        Text(
                            "Oops! You haven't placed any orders yet",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W500
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp
                        )
                    ) {
                        val sortedOrders = orders.sortedByDescending { order ->
                            order.createdDate
                        }

                        items(sortedOrders, { it.createdDate }) { order ->
                            OrderItem(
                                order = order,
                                reviewViewModel = reviewViewModel,
                                reviewUiState = reviewUiState,
                                navController = navController
                            )
                        }
                    }

                }

            }
        }

        if (orderHistoryUiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent.copy(alpha = 0.2f))
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
fun OrderItem(
    modifier: Modifier = Modifier,
    order: Order,
    reviewViewModel: ReviewViewModel,
    reviewUiState: ReviewUiState,
    navController: NavController
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Surface(
            color = Color.LightGray.copy(0.4f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(MaterialTheme.dimens.small1),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            "Order:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            order.publicOrderId,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            "Placed On: ",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Text(
                            formatEpochMillis(order.createdDate),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        order.orderStatus.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            "Expected Delivery: ",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            formatEpochMillis(order.deliveryDate),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OrderRowSection(
                    order = order,
                    reviewViewModel = reviewViewModel,
                    reviewUiState = reviewUiState,
                    navController = navController
                )

                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}

@Composable
fun OrderRowSection(
    modifier: Modifier = Modifier,
    order: Order,
    reviewViewModel: ReviewViewModel,
    reviewUiState: ReviewUiState,
    navController: NavController
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            order.orderItems.forEach { orderItem ->
                OrderItemRow(
                    itemName = orderItem.productName,
                    itemPrice = orderItem.price,
                    itemCount = orderItem.quantity
                )
                if (!orderItem.isRated) {
                    RatingCard(
                        maxStars = 5,
                        stars = reviewUiState.stars,
                        onStarsChange = { stars ->
                            reviewViewModel.resetReviewState()
                            reviewViewModel.onStarsChange(stars)
                            reviewViewModel.setOrderType(targetId = orderItem.id)
                            navController.navigate(Screen.ReviewScreen.route)

                        }
                    )
                }
                HorizontalDivider(
                    thickness = 0.2.dp,
                    modifier = Modifier.padding(horizontal = 2.dp),
                    color = Color.Gray
                )
            }

            OrderItemRow(
                itemName = "Shipping Fee",
                itemPrice = order.shippingFee,
            )
            HorizontalDivider(
                thickness = 0.2.dp,
                modifier = Modifier.padding(horizontal = 2.dp),
                color = Color.Gray
            )
            val address = order.address
            val addressDescription = if (address.houseNumber.isBlank()) {
                "${address.landmark}, ${address.street}, ${address.city}, ${address.state}, ${address.pinCode}"
            } else {
                "${address.houseNumber}, ${address.landmark}, ${address.street}, ${address.city}, ${address.state}, ${address.pinCode}"
            }
            OrderTotalRow(
                address = addressDescription,
                total = order.totalAmount
            )
        }

    }

}

@Composable
fun OrderItemRow(
    modifier: Modifier = Modifier,
    itemName: String,
    itemPrice: Double,
    itemCount: Int? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.45f),
            text = itemName,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        if (itemCount == null) {
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = formatCurrency(itemPrice),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "$itemCount pcs",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = formatCurrency(itemCount * itemPrice),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

        }
    }
}

@Composable
fun OrderTotalRow(modifier: Modifier = Modifier, address: String, total: Double) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.LocationOn,
                modifier = Modifier.size(15.dp),
                contentDescription = "Location",
                tint = Color.Gray
            )
            Text(
                modifier = Modifier.fillMaxWidth(0.4f),
                text = address,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = Color.Gray
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Total",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = Color.Gray
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = formatCurrency(total),
                style = MaterialTheme.typography.bodySmall,
//                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
//used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Orders"
    ) {

        Icon(
            painterResource(R.drawable.bag_outline),
            contentDescription = "cart",
            modifier = Modifier
                .size(21.dp)
                .bounceClick {
                    navController.navigate(Screen.CartScreen.route)
                }
        )
    }
}