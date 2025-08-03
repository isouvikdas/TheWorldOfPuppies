package com.example.theworldofpuppies.shop.product.presentation

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.navigation.Screen
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
    navController: NavController
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
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
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
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
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
                        }

                    )
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
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
                        }

                    )
                }
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
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
    onSeeAllClick: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.extraLarge3 + MaterialTheme.dimens.large3)
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
                    .clickable(enabled = !products.isEmpty()) { onSeeAllClick() }
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
                                }
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
    onProductSelect: () -> Unit
) {
    Surface(
        modifier = modifier
            .width(MaterialTheme.dimens.extraLarge2)
            .fillMaxHeight()
            .clickable { onProductSelect() },
        shape = RoundedCornerShape(MaterialTheme.dimens.small1),
        shadowElevation = 5.dp,
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
                    .height(MaterialTheme.dimens.extraLarge3),
                shape = RoundedCornerShape(
                    topStart = MaterialTheme.dimens.small1,
                    topEnd = MaterialTheme.dimens.small1
                ),
                color = Color.LightGray.copy(0.4f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.firstImageUri)
                        .crossfade(true)
                        .error(R.drawable.login)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f))
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(MaterialTheme.dimens.small1))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .padding(horizontal = MaterialTheme.dimens.extraSmall),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatCurrency(product.price),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier,
                        fontWeight = FontWeight.SemiBold
                    )

                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .height(35.dp)
                            .width(80.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.secondary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
                                0.3f
                            ),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.W600
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
                    .clickable(enabled = !categories.isEmpty()) { }
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

