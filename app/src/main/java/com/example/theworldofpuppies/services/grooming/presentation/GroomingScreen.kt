package com.example.theworldofpuppies.services.grooming.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.example.theworldofpuppies.services.grooming.domain.GroomingFeature
import com.example.theworldofpuppies.services.grooming.domain.GroomingUiState
import com.example.theworldofpuppies.services.utils.presentation.ServiceTopAppBar
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GroomingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    groomingUiState: GroomingUiState = GroomingUiState()
) {

    val serviceFeatures = groomingUiState.serviceFeatures
    val discount = groomingUiState.discount

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

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
            Box(modifier = Modifier.fillMaxSize()) {
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
                                    text = "Clean Grooming service without parabens, phthalates and chemical dyes.",
                                    fontWeight = FontWeight.W500,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 5.dp)
                                )
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
                                    modifier = Modifier.size(MaterialTheme.dimens.small1.times(3) / 2)
                                )
                                Text(
                                    text = "(4.2)",
                                    style = MaterialTheme.typography.titleSmall,
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
                                        .padding(MaterialTheme.dimens.extraSmall.times(3) / 2),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "24%",
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

                    item { Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1)) }

                    items(serviceFeatures) { serviceFeature ->
                        ServiceFeatureItem(serviceFeature = serviceFeature, modifier = Modifier.padding(vertical = 8.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraLarge1))
                    }

                }

                GroomingBottomSection(modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}

@Composable
fun ServiceFeatureItem(
    modifier: Modifier = Modifier,
    serviceFeature: GroomingFeature
) {
    val features = serviceFeature.features
    val price = serviceFeature.price
    val discountedPrice = serviceFeature.discountedPrice
    val title = serviceFeature.title

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
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
                            text = title,
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

                    Column {
                        Text(
                            text = formatCurrency(discountedPrice),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Text(
                            text = formatCurrency(price),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W600,
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(0.8f),
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
fun GroomingBottomSection(modifier: Modifier = Modifier) {
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
                }
        )
    }
}


