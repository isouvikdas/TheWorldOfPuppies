package com.example.theworldofpuppies.shop.product.presentation.product_detail

import android.net.Uri
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.cart.presentation.CartQuantitySection
import com.example.theworldofpuppies.shop.cart.presentation.CartViewModel
import com.example.theworldofpuppies.shop.product.domain.Image
import com.example.theworldofpuppies.shop.product.presentation.ErrorSection
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel
import com.example.theworldofpuppies.ui.theme.dimens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    productDetailState: ProductDetailState,
    productViewModel: ProductViewModel? = null,
    cartViewModel: CartViewModel? = null,
    navController: NavController
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val product = productDetailState.product
    val images = product?.images ?: emptyList()
    val finalImageList = images.toMutableList().apply {
        if (product?.firstImage != null)
            add(product.firstImage)
    }
    val discount = remember(product?.discount) { product?.discount ?: 0 }
    val description = product?.description.orEmpty()
    val price = remember(product?.price) { product?.price ?: 0.0 }
    val discountedPrice = remember(product?.discountedPrice) { product?.discountedPrice ?: 0.0 }
    val productName = remember(product?.name) { product?.name ?: "" }
    val productId = remember(product?.id) { product?.id ?: "" }
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
            ProductHeader(
                navController = navController,
                scrollBehavior = scrollBehavior
            )
        }

    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.Transparent
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        ProductImageSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.large3.times(2)),
                            images = finalImageList
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
                            discount = discount,
                            productName = productName,
                            description = description,
                            originalPrice = price,
                            discountedPrice = discountedPrice,
                            isRated = product?.isRated ?: false,
                            averageStars = product?.averageStars ?: 0.0,
                            totalReviews = product?.totalReviews ?: 0

                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
                    }
                }

                ProductBottomSection(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    cartViewModel = cartViewModel,
                    discountedPrice = discountedPrice,
                    productId = productId,
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
//used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Product Details"
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductImageSection(
    images: List<Image>?,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = images?.size ?: 0,
        initialPage = 0
    )

    Column(
        modifier = modifier
    ) {
        if (!images.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.large3.times(2) - MaterialTheme.dimens.small2)
                        .padding(top = MaterialTheme.dimens.small1)
                ) { page ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = MaterialTheme.dimens.small1.div(5).times(4)),
                        shape = RoundedCornerShape(MaterialTheme.dimens.small2),
                        shadowElevation = 1.dp,
                        color = Color.White
                    ) {
                        ProductImage(images[page])
                    }
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.dimens.small1,
                            end = MaterialTheme.dimens.small1,
                            top = MaterialTheme.dimens.small1
                        ),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = Color.Gray
                )
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = MaterialTheme.dimens.small1,
                        start = MaterialTheme.dimens.small1.div(5).times(4),
                        end = MaterialTheme.dimens.small1.div(5).times(4)
                    ),
                shape = RoundedCornerShape(MaterialTheme.dimens.small2),
                shadowElevation = 1.dp,
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
fun ProductDetailSection(
    modifier: Modifier = Modifier,
    productName: String?,
    description: String?,
    discount: Int,
    originalPrice: Double,
    discountedPrice: Double,
    isRated: Boolean = false,
    averageStars: Double = 0.0,
    totalReviews: Int = 0
) {
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
                text = productName.toString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth(0.65f),
                fontWeight = FontWeight.W600
            )
            /*Discount Card*/
            if (discount.toString().isNotEmpty() && discount > 0) {
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

        /*Product price section*/
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "MRP :",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.W400,
            )
            /*without discounted price*/
            Text(
                text = formatCurrency(originalPrice),
                style = MaterialTheme.typography.headlineSmall,
                textDecoration = TextDecoration.LineThrough,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            /*discounted price*/
            Text(
                text = formatCurrency(discountedPrice),
                style = MaterialTheme.typography.headlineMedium,
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
                    if (isRated && averageStars > 0.0 && totalReviews > 0) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC700)
                        )
                        Text(
                            text = "$averageStars",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier,
                            fontWeight = FontWeight.W500
                        )
                        Text (
                            " ~ (${totalReviews})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.W500,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }


                }
            }
            var isExpanded by remember { mutableStateOf(false) }
            Text(
                text = description.toString(),
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
fun ProductImage(image: Image?) {
    if (image?.fetchUrl?.toUri() != Uri.EMPTY) {
        AsyncImage(
            model = image?.fetchUrl?.toUri(),
            contentDescription = "product image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit,
            error = painterResource(R.drawable.login)
        )
    } else {
        Image(
            painterResource(R.drawable.login),
            contentDescription = "Pet profile pic",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProductBottomSection(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel? = null,
    discountedPrice: Double,
    productId: String,
) {
    val quantity = remember { mutableIntStateOf(0) }
    val totalPrice = remember(discountedPrice, quantity.intValue) {
        mutableDoubleStateOf(discountedPrice * quantity.intValue)
    }
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
                    start = MaterialTheme.dimens.small1.div(5).times(4),
                    end = MaterialTheme.dimens.small1.div(5).times(4),
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
                        .wrapContentHeight(),
                    quantity = quantity.intValue,
                    increaseQuantity = { quantity.intValue += 1 },
                    decreaseQuantity = { if (quantity.intValue > 0) quantity.intValue -= 1 },
                )
                Row(
                    modifier = Modifier
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Total :",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = formatCurrency(totalPrice.doubleValue),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }

            Button(
                onClick = {
                    cartViewModel?.addToCart(
                        productId = productId,
                        quantity = quantity.intValue,
                        isItProductScreen = true
                    )
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
                Text(
                    text = "Add to cart",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}