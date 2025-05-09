package com.example.theworldofpuppies.shop.product.presentation.product_detail

import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SignalWifiStatusbarNull
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.shop.product.presentation.ErrorSection
import com.example.theworldofpuppies.ui.theme.dimens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(modifier: Modifier = Modifier) {

    val navController: NavHostController = rememberNavController()
    val productDetailState = ProductDetailState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            ProductHeader(modifier = Modifier)
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
            Column(modifier = Modifier.fillMaxSize()) {
                ProductImageSection(
                    productDetailState = productDetailState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.large3.times(2))
                        .padding(horizontal = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
                ProductDetailSection(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHeader(modifier: Modifier = Modifier) {
    var isLiked by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4),
        navigationIcon = {

            Icon(
                Icons.AutoMirrored.Default.ArrowBackIos,
                contentDescription = "Menu",
                modifier = Modifier
                    .size(MaterialTheme.dimens.small1 + MaterialTheme.dimens.extraSmall)
                    .clickable {}
            )
        },
        title = {
            Text(
                text = "Product Detail",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.W500
            )
        },
        actions = {
            IconButton(
                onClick = { isLiked = !isLiked }
            ) {
                when (isLiked) {
                    true -> Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Liked",
                        tint = Color.Red,
                    )

                    false -> Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Liked",
                    )
                }
            }
            IconButton(onClick = { /* Bag action */ }) {
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
                    shadowElevation = 20.dp
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
                shadowElevation = 20.dp
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
            .background(Color.LightGray.copy(0.2f)),
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
fun ProductDetailSection(modifier: Modifier = Modifier) {
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
                text = "Pet Shampoo",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .fillMaxWidth(0.65f),
                fontWeight = FontWeight.W600
            )
            /*Discount Card*/
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(MaterialTheme.dimens.extraSmall)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {

                Text(
                    text = "25% off",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.extraSmall.times(3) / 2),
                    fontWeight = FontWeight.W500,
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
                text = "$199.00",
                style = MaterialTheme.typography.headlineLarge,
                textDecoration = TextDecoration.LineThrough,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.W500,
                color = Color.Gray
            )
            /*discounted price*/
            Text(
                text = "$149.00",
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
                text = stringResource(R.string.product_description),
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
            .height(MaterialTheme.dimens.extraLarge3)
            .clip(
                RoundedCornerShape(
                    topStart = MaterialTheme.dimens.small3,
                    topEnd = MaterialTheme.dimens.small3
                )
            ),
        color = Color.White,
        shadowElevation = 20.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(0.2f))
                .padding(
                    start = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4,
                    end = MaterialTheme.dimens.small1 + MaterialTheme.dimens.small1 / 4,
                    top = MaterialTheme.dimens.small2,
                    bottom = MaterialTheme.dimens.small1
                )
                .padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
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
                        .fillMaxWidth(0.5f)
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(MaterialTheme.dimens.small1.times(3) / 2)
                            .clip(CircleShape)
                            .clickable {}
                            .background(Color.White),
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
                        text = "02",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.W500
                    )

                    Box(
                        modifier = Modifier
                            .size(MaterialTheme.dimens.small1.times(3) / 2)
                            .clip(CircleShape)
                            .clickable {}
                            .background(Color.White),
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