package com.example.theworldofpuppies.shop.product.presentation

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.cart.presentation.CartViewModel
import com.example.theworldofpuppies.shop.product.domain.Category
import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.shop.product.domain.util.ListType
import com.example.theworldofpuppies.shop.product.presentation.product_list.CategoryListState
import com.example.theworldofpuppies.shop.product.presentation.product_list.FeaturedProductListState
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductListState
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel
import com.example.theworldofpuppies.shop.product.presentation.util.toString
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopHomeScreen(
    modifier: Modifier = Modifier,
    productListState: ProductListState,
    categoryListState: CategoryListState,
    featuredProductListState: FeaturedProductListState,
    productViewModel: ProductViewModel? = null,
    onProductSelect: () -> Unit,
    getCategories: () -> Unit,
    getProducts: () -> Unit,
    getFeaturedProducts: () -> Unit,
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val lazyListState = rememberLazyListState()
    val imageList = List(10) { painterResource(id = R.drawable.pet_banner) }
    val productList =
        remember(productListState.productList) { productListState.productList.take(4) }
    val categoryList =
        remember(categoryListState.categoryList) { categoryListState.categoryList.take(6) }
    val featuredProductList =
        remember(featuredProductListState.productList) { featuredProductListState.productList.take(4) }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Color.Transparent
    ) {
        when (productListState.isLoading && categoryListState.isLoading && featuredProductListState.isLoading) {
            true -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            false -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = lazyListState
            ) {
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                }

                item {
                    ProductBannerSection(modifier = Modifier, imageList = imageList)
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                }

                item {
                    ProductCategorySection(
                        modifier = Modifier,
                        categories = categoryList,
                        isLoading = categoryListState.isLoading,
                        getCategories = { getCategories() },
                        errorMessage = categoryListState.errorMessage ?: ""
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    ProductRowSection(
                        modifier = Modifier,
                        productListType = ListType.FEATURED,
                        products = featuredProductList,
                        isLoading = featuredProductListState.isLoading,
                        errorMessage = featuredProductListState.errorMessage ?: "",
                        getProducts = { getFeaturedProducts() },
                        onProductSelect = { onProductSelect() },
                        productViewModel = productViewModel,
                        onSeeAllClick = {
                            productViewModel?.setListType(ListType.FEATURED)
                            navController.navigate(Screen.ProductListScreen.route)
                        },
                        cartViewModel = cartViewModel
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    ProductRowSection(
                        modifier = Modifier,
                        productListType = ListType.NEW,
                        products = productList,
                        isLoading = productListState.isLoading,
                        getProducts = { getProducts() },
                        errorMessage = productListState.errorMessage ?: "",
                        onProductSelect = { onProductSelect() },
                        productViewModel = productViewModel,
                        onSeeAllClick = {
                            productViewModel?.setListType(ListType.NEW)
                            navController.navigate(Screen.ProductListScreen.route)
                        },
                        cartViewModel = cartViewModel

                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    ProductRowSection(
                        modifier = Modifier,
                        productListType = ListType.ALL,
                        products = productList,
                        isLoading = productListState.isLoading,
                        getProducts = { getProducts() },
                        errorMessage = productListState.errorMessage ?: "",
                        onProductSelect = { onProductSelect() },
                        productViewModel = productViewModel,
                        onSeeAllClick = {
                            productViewModel?.setListType(ListType.ALL)
                            navController.navigate(Screen.ProductListScreen.route)
                        },
                        cartViewModel = cartViewModel

                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun ProductRowSection(
    modifier: Modifier = Modifier,
    productListType: ListType,
    products: List<Product>,
    isLoading: Boolean? = false,
    errorMessage: String? = null,
    getProducts: () -> Unit,
    onProductSelect: () -> Unit,
    productViewModel: ProductViewModel? = null,
    onSeeAllClick: () -> Unit,
    cartViewModel: CartViewModel
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = MaterialTheme.dimens.small1),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = productListType.toString(context),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = stringResource(R.string.see_all),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.titleSmall,
                textDecoration = if (products.isEmpty()) TextDecoration.LineThrough else TextDecoration.None,
                fontStyle = if (products.isEmpty()) FontStyle.Italic else FontStyle.Normal,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .bounceClick { if (!products.isEmpty()) onSeeAllClick() }
            )

        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading == true -> {
                    CircularProgressIndicator()
                }

                !errorMessage.isNullOrBlank() -> {
                    ErrorSection(
                        modifier = Modifier.fillMaxSize(),
                        errorMessage = errorMessage
                    ) {
                        getProducts()
                    }
                }

                products.isNotEmpty() -> {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        items(products, key = { it.id }) { product ->
                            ProductItem(
                                modifier = Modifier
                                    .padding(
                                        start = if (product == products.first()) MaterialTheme.dimens.small1
                                        else MaterialTheme.dimens.small1.div(2),
                                        end = if (product == products.last()) MaterialTheme.dimens.small1 else 0.dp
                                    )
                                    .padding(vertical = MaterialTheme.dimens.small1),
                                product = product,
                                onProductSelect = {
                                    onProductSelect()
                                    productViewModel?.setProduct(product)
                                },
                                cartViewModel = cartViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onProductSelect: () -> Unit,
    cartViewModel: CartViewModel
) {
    Surface(
        modifier = modifier
            .width(MaterialTheme.dimens.extraLarge2)
            .height(240.dp)
            .clickable { onProductSelect() },
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                ),
                color = Color.LightGray.copy(0.4f)
            ) {
                if (product.firstImage?.fetchUrl?.toUri() != Uri.EMPTY) {
                    AsyncImage(
                        model = product.firstImage?.fetchUrl?.toUri(),
                        contentDescription = "Product image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit,
                        error = painterResource(R.drawable.login)
                    )
                } else {
                    Image(
                        painterResource(R.drawable.login),
                        contentDescription = "product image",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f))
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    fontWeight = FontWeight.W500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 1.dp)
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (product.discount > 0) {
                        Text(
                            text = formatCurrency(product.price),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier,
                            fontWeight = FontWeight.W500,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            fontStyle = FontStyle.Italic
                        )

                    }
                    Text(
                        text = formatCurrency(product.discountedPrice),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (product.discount > 0) {
                        Text(
                            text = product.discount.toString() + "% OFF",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(0.7f)
                        )

                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC700)
                        )
                        Text(
                            text = "(4.2)",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier,
                            fontWeight = FontWeight.W500
                        )
                    }
                    FloatingActionButton(
                        onClick = { cartViewModel.addToCart(product.id, 1, true) },
                        modifier = Modifier
                            .size(40.dp),
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                }
            }

        }
    }

}

@Composable
fun ProductBannerSection(modifier: Modifier = Modifier, imageList: List<Painter>) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.extraLarge2),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        items(imageList) { image ->
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .padding(
                        start = MaterialTheme.dimens.small1,
                        end = if (image == imageList.last()) MaterialTheme.dimens.small1 else 0.dp
                    )
                    .clickable {},
                color = Color.White,
                shape = RoundedCornerShape(MaterialTheme.dimens.small2)
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCategorySection(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    getCategories: () -> Unit,
    isLoading: Boolean? = false,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.large2 + MaterialTheme.dimens.small2),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.see_all),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.titleSmall,
                textDecoration = if (categories.isEmpty()) TextDecoration.LineThrough else TextDecoration.None,
                fontStyle = if (categories.isEmpty()) FontStyle.Italic else FontStyle.Normal,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .bounceClick {}
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                categories.isEmpty() && isLoading == true -> {
                    CircularProgressIndicator()
                }

                categories.isEmpty() && !errorMessage.isNullOrBlank() -> {
                    ErrorSection(modifier = Modifier.fillMaxSize(), errorMessage = errorMessage) {
                        getCategories()
                    }
                }

                categories.isNotEmpty() -> {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        items(categories) { category ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        start = MaterialTheme.dimens.small1,
                                        end = if (category == categories.last()) MaterialTheme.dimens.small1 else 0.dp
                                    ),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .size(MaterialTheme.dimens.medium2)
                                        .clip(CircleShape),
                                    color = Color.Gray.copy(0.3f)

                                ) {
                                    // Put your Image/Icon here
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.W500
                                )
                            }
                        }

                    }
                }
            }
        }

    }

}


@Composable
fun ErrorSection(
    modifier: Modifier = Modifier,
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))
        Text(
            text = "Retry",
            style = MaterialTheme.typography.titleMedium,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onRetry() }
        )
    }
}

