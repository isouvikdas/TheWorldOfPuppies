package com.example.theworldofpuppies.shop.product.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopHomeScreen(
    modifier: Modifier = Modifier,
    productListState: ProductListState,
    categoryListState: CategoryListState? = null,
    productViewModel: ProductViewModel? = null,
    onProductSelect: () -> Unit
) {
    val imageList = List(10) { painterResource(id = R.drawable.flag_india) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        color = Color.Transparent
    ) {
        when (productListState.isLoading) {
            true -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            false -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                }

                item {
                    ProductBannerSection(modifier = Modifier, imageList = imageList)
                }

                item {
                    ProductCategorySection(modifier = Modifier)
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                }

                item {
                    ProductRowSection(
                        modifier = Modifier,
                        productListType = "Featured Products",
                        products = productListState.productList
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                }

                item {
                    ProductRowSection(
                        modifier = Modifier,
                        productListType = "Popular Products",
                        products = productListState.productList
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                }

                item {
                    ProductRowSection(
                        modifier = Modifier,
                        productListType = "New Launches",
                        products = productListState.productList
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
fun ProductRowSection(modifier: Modifier = Modifier, productListType: String, products: List<Product>) {
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
                text = productListType,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "See all", style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                modifier = Modifier.clickable {}
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            items(products.take(4)) { product->
                ProductItem(modifier = Modifier, product = product)
            }
        }

    }

}

@Composable
fun ProductItem(modifier: Modifier = Modifier, product: Product) {
    ElevatedCard(
        modifier = modifier
            .width(MaterialTheme.dimens.extraLarge3)
            .fillMaxHeight()
            .padding(vertical = MaterialTheme.dimens.small1)
            .padding(horizontal = MaterialTheme.dimens.small1 / 2)
            .clickable{},
        shape = RoundedCornerShape(MaterialTheme.dimens.small1),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.extraLarge3),
                shape = RoundedCornerShape(
                    topStart = MaterialTheme.dimens.small1,
                    topEnd = MaterialTheme.dimens.small1
                ),
                colors = CardDefaults.cardColors(Color.LightGray.copy(0.2f))
            ) {
                if (product.firstImageUri == null) {
                    Image(
                        painter = painterResource(id = R.drawable.login),
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    AsyncImage(
                        model = product.firstImageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
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
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodyLarge,
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
                        contentColor = MaterialTheme.colorScheme.onPrimary,
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

@Composable
fun ProductBannerSection(modifier: Modifier = Modifier, imageList: List<Painter>) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.extraLarge1),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        items(imageList) { image ->
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .padding(
                        start = MaterialTheme.dimens.small1,
                        end = if (image == imageList.last()) MaterialTheme.dimens.small1 else 0.dp
                    )
                    .clip(RoundedCornerShape(MaterialTheme.dimens.small2))
                    .shadow(elevation = 10.dp)
                    .clickable {}
            )
        }
    }

}

@Composable
fun ProductCategorySection(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .height(MaterialTheme.dimens.large1)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        items(10) { item ->
            Box(
                modifier = Modifier
                    .padding(
                        start = MaterialTheme.dimens.small1,
                    )
            ) {
                Card(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.medium2)
                        .clip(CircleShape),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(0.2f)
                    )
                ) {

                }

            }

        }
    }

}