package com.example.theworldofpuppies.services.grooming.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.grooming.domain.GroomingSubService
import com.example.theworldofpuppies.services.grooming.domain.GroomingUiState
import com.example.theworldofpuppies.services.core.presentation.component.ServiceTopAppBar
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun GroomingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    groomingViewModel: GroomingViewModel,
    groomingUiState: GroomingUiState
) {
    val grooming = groomingUiState.grooming
    val discount = grooming?.discount
    val description = grooming?.description
    val showFullscreenLoader = grooming == null && groomingUiState.isLoading
    val isRefreshing = grooming != null && groomingUiState.isLoading

    val selectedServiceId = groomingUiState.selectedSubServiceId

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        groomingViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


    val pullToRefreshState = rememberPullToRefreshState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    LaunchedEffect(Unit) {
        if (grooming == null) {
            groomingViewModel.loadGrooming()
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
                GroomingHeader(
                    navController = navController,
                    scrollBehavior = scrollBehavior
                )
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), color = Color.Transparent
            ) {
                if (groomingUiState.error != null && grooming == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painterResource(R.drawable.dog_sad),
                            contentDescription = "dog",
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            "Oops! Something went wrong",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = { groomingViewModel.loadGrooming(forceRefresh = true) },
                            state = pullToRefreshState
                        ) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = MaterialTheme.dimens.small1)
                                            .padding(top = MaterialTheme.dimens.extraSmall),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        description?.let {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Surface(
                                                    modifier = Modifier.size(48.dp),
                                                    color = Color.White,
                                                    shape = CircleShape
                                                ) {
                                                    Image(
                                                        painterResource(R.drawable.grooming_icon),
                                                        contentDescription = null
                                                    )
                                                }
                                                Text(
                                                    text = description,
                                                    fontWeight = FontWeight.W500,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.padding(start = 5.dp)
                                                )
                                            }

                                        }

                                    }
                                }

                                item {
                                    Row(
                                        modifier = Modifier
                                            .wrapContentHeight()
                                            .fillMaxWidth()
                                            .padding(horizontal = MaterialTheme.dimens.small1)
                                            .padding(top = MaterialTheme.dimens.extraSmall),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier.size(
                                                    MaterialTheme.dimens.small1.times(
                                                        3
                                                    ) / 2
                                                )
                                            )
                                            Text(
                                                text = "(4.2)",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.W500
                                            )
                                            Text(
                                                "~",
                                                style = MaterialTheme.typography.displaySmall,
                                                fontWeight = FontWeight.W400,
                                                modifier = Modifier.padding(horizontal = 3.dp)
                                            )
                                            Text(
                                                text = "(1,268 ratings)",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Color.Gray
                                            )
                                        }

                                        discount?.let {
                                            Box(
                                                modifier = Modifier.size(55.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.discount_badge),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.tertiary
                                                )
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(
                                                            MaterialTheme.dimens.extraSmall.times(
                                                                3
                                                            ) / 2
                                                        ),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = "$discount%",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                    Text(
                                                        text = "OFF",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                item { Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1)) }
                                if (groomingUiState.subServices.isNotEmpty()) {
                                    items(groomingUiState.subServices) { serviceFeature ->
                                        val isSelected = selectedServiceId == serviceFeature.id
                                        ServiceFeatureItem(
                                            serviceFeature = serviceFeature,
                                            modifier = Modifier.padding(vertical = 16.dp),
                                            isSelected = isSelected,
                                            onClick = {
                                                groomingViewModel.selectSubService(serviceFeature.id)
                                            }
                                        )
                                    }
                                    item {
                                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraLarge1))
                                    }
                                }
                            }
                        }



                        GroomingBottomSection(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            onBookNowClick = {
                                groomingViewModel.onBookNowClick(navController)
                            })
                    }

                }
            }
        }

        if (showFullscreenLoader) {
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
fun ServiceFeatureItem(
    modifier: Modifier = Modifier,
    serviceFeature: GroomingSubService,
    onClick: () -> Unit = {},
    isSelected: Boolean
) {
    val features = serviceFeature.features
    val price = serviceFeature.price
    val discountedPrice = serviceFeature.discountedPrice
    val name = serviceFeature.name

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1)
            .clickable {
                onClick()
            },
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isSelected) 8.dp else 0.dp
    ) {
        Surface(color = Color.Transparent) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.LightGray.copy(0.6f))
                        .padding(
                            horizontal = MaterialTheme.dimens.small1,
                            vertical = MaterialTheme.dimens.small1.div(2)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(38.dp),
                            shape = CircleShape,
                            color = Color.Transparent
                        ) {
                            Image(
                                painterResource(R.drawable.bathtub),
                                contentDescription = null
                            )
                        }
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(
                                start = MaterialTheme.dimens.small1.div(
                                    2
                                )
                            ),
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = formatCurrency(discountedPrice),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Text(
                            text = formatCurrency(price),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W600,
                            color = Color.Gray.copy(0.8f),
                            textDecoration = TextDecoration.LineThrough,
                            fontStyle = FontStyle.Italic
                        )

                    }
                }

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.LightGray.copy(0.1f))
                        .padding(MaterialTheme.dimens.small1),
                ) {
                    features.forEach { feature ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.size(18.dp)
                            )

                            Text(
                                feature,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier.padding(
                                    start = MaterialTheme.dimens.small1.div(
                                        2
                                    )
                                )
                            )
                        }
                    }
                }

            }

        }

    }

}

@Composable
fun GroomingBottomSection(modifier: Modifier = Modifier, onBookNowClick: () -> Unit) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .zIndex(1f),
        color = Color.White,
        shape = RoundedCornerShape(
            topStart = MaterialTheme.dimens.small3,
            topEnd = MaterialTheme.dimens.small3
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.LightGray.copy(0.55f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            Button(
                onClick = {
                    onBookNowClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.buttonHeight)
                    .padding(horizontal = MaterialTheme.dimens.small1),
                shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
            ) {
                Text(
                    text = "Book Now",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroomingHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    ServiceTopAppBar(
        scrollBehavior = scrollBehavior,
        navController = navController,
        title = "Grooming & Bath"
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


