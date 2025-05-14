package com.example.theworldofpuppies.shop.product.presentation.product_detail

import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SignalWifiStatusbarNull
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingBag
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.shop.cart.presentation.CartQuantitySection
import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.shop.product.presentation.ErrorSection
import com.example.theworldofpuppies.ui.theme.dimens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    productDetailState: ProductDetailState,
    onBack: () -> Unit,
    onCartClick: () -> Unit
) {
    val discount = 25

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            ProductHeader(
                modifier = Modifier,
                onBack = { onBack() },
                onCartClick = { onCartClick() })
        },
        bottomBar = {
            ProductBottomSection(modifier = Modifier)
        }

    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.Transparent
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    ProductImageSection(
                        productDetailState = productDetailState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.dimens.large3.times(2))
                            .padding(horizontal = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
                }
                item {
                    ProductDetailSection(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4),
                        product = productDetailState.product,
                        discount = discount
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHeader(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onCartClick: () -> Unit
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1.div(4)),
        navigationIcon = {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier
                    .size(
                        MaterialTheme.dimens.small1 + MaterialTheme.dimens.extraSmall.times(3)
                            .div(2)
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Menu",
                    modifier = Modifier
                        .size(MaterialTheme.dimens.small2)
                )

            }
        },
        title = {
            Text(
                text = "Product",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall)
            )
        },
        actions = {
            IconButton(
                onClick = { onCartClick() },
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall)
            ) {
                Icon(
                    Icons.Outlined.ShoppingBag,
                    contentDescription = "Bag",
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductImageSection(productDetailState: ProductDetailState, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(
        pageCount = productDetailState.images.size,
        initialPage = 0
    )

    Column(
        modifier = modifier
    ) {
        if (productDetailState.images.isNotEmpty()) {
            Column {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = MaterialTheme.dimens.small1),
                    shape = RoundedCornerShape(MaterialTheme.dimens.small2),
                    shadowElevation = 5.dp
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        ProductImage(productDetailState.images[page])
                    }
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .padding(16.dp),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = Color.Gray
                )
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = MaterialTheme.dimens.small1),
                shape = RoundedCornerShape(MaterialTheme.dimens.small2),
                shadowElevation = 5.dp,
                color = Color.White
            ) {
                PlaceholderForEmptyImages()
            }
        }

    }
}

@Composable
fun PlaceholderForEmptyImages() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(0.4f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.login),
            contentDescription = "image error",
            contentScale = ContentScale.Fit
        )
        ErrorSection(
            modifier = Modifier.fillMaxSize(),
            errorMessage = "Couldn't get the product images",
            onRetry = {

            })
    }
}


@Composable
fun ProductDetailSection(modifier: Modifier = Modifier, product: Product?, discount: Int) {

    val discountedPrice = product?.price?.times(100 - discount)?.div(100)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.extraSmall.times(3) / 2)
    ) {

        /*Product name and discount row*/
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product?.name.toString(),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .fillMaxWidth(0.65f),
                fontWeight = FontWeight.W600
            )
            /*Discount Card*/
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(MaterialTheme.dimens.extraSmall.times(3).div(2)),
                color = MaterialTheme.colorScheme.tertiary,
                shadowElevation = 1.dp
            ) {

                Text(
                    text = "$discount% Off",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.extraSmall.times(3) / 2),
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        /*Product price section*/
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(vertical = MaterialTheme.dimens.small1 / 3),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
        ) {
            Text(
                text = "MRP :",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.W400,
            )
            /*without discounted price*/
            Text(
                text = "$${product?.price.toString()}",
                style = MaterialTheme.typography.headlineLarge,
                textDecoration = TextDecoration.LineThrough,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            /*discounted price*/
            Text(
                text = "$${discountedPrice.toString()}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }

        /*Product description*/
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W600
                )
                Row(
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(MaterialTheme.dimens.small1.times(3) / 2)
                    )
                    Text(
                        text = "(4.2)",
                        style = MaterialTheme.typography.titleMedium
                    )

                }
            }
            var isExpanded by remember { mutableStateOf(false) }
            Text(
                text = product?.description.toString(),
                modifier = Modifier
                    .padding(
                        vertical = MaterialTheme.dimens.extraSmall
                    )
                    .clickable { isExpanded = !isExpanded },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Unspecified,
                maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                color = Color.Black.copy(0.5f),
                overflow = TextOverflow.Ellipsis
            )

        }


    }

}

@Composable
fun ProductImage(base64Image: String) {
    val byteArray = Base64.decode(base64Image, Base64.DEFAULT)

    if (base64Image.isEmpty()) {
        Image(
            imageVector = Icons.Default.SignalWifiStatusbarNull,
            contentDescription = null,
            modifier = Modifier.size(70.dp),
            contentScale = ContentScale.Fit
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(byteArray)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun ProductBottomSection(modifier: Modifier = Modifier) {
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
                    start = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4,
                    end = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4,
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
                CartQuantitySection(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .wrapContentHeight()
                )
                Row(
                    modifier = Modifier
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Total :",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = "$298.00",
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
                    text = "Add to cart",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}