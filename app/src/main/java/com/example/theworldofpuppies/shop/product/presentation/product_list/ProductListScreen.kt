package com.example.theworldofpuppies.shop.product.presentation.product_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.shop.product.presentation.ProductItem
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun ProductListScreen(
    productList: List<Product>,
    onProductSelect: (Product) -> Unit,
    enablePagination: Boolean = false,
    isLoading: Boolean = false,
    onLoadMore: (() -> Unit)? = null,
    productTypeLabel: String = ""
) {
    val lazyGridState = rememberLazyGridState()
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = lazyGridState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.extraSmall),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.extraSmall)
        ) {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = productTypeLabel,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start
                    )
                    DropDownDemo()
                }
            }
            items(productList) { product ->
                ProductItem(
                    product = product,
                    onProductSelect = {
                        onProductSelect(product)
                    }
                )
            }
        }
    }

    if (enablePagination && onLoadMore != null) {
        LaunchedEffect(lazyGridState) {
            snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo }
                .collect { visibleItems ->
                    val totalItems = lazyGridState.layoutInfo.totalItemsCount
                    val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0

                    if (lastVisibleItemIndex >= totalItems - 3 && !isLoading) {
                        onLoadMore()
                    }
                }
        }
    }


}

@Composable
fun DropDownDemo() {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableIntStateOf(0) }
    val usernames = listOf("Alexander", "Isabella", "Benjamin", "Sophia", "Christopher")

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        GlassMorphismCard(
            modifier = Modifier
                .padding(32.dp)
                .wrapContentSize(),
            blurRadius = 32.dp
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            isDropDownExpanded.value = true
                        }
                ) {
                    Text(text = usernames[itemPosition.intValue])
                    Icon(
                        if (isDropDownExpanded.value) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = { isDropDownExpanded.value = false }
                ) {
                    usernames.forEachIndexed { index, username ->
                        DropdownMenuItem(
                            text = { Text(username) },
                            onClick = {
                                itemPosition.intValue = index
                                isDropDownExpanded.value = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassMorphismCard(
    modifier: Modifier = Modifier,
    blurRadius: Dp = 16.dp,
    backgroundColor: Color = Color.White.copy(0.15f),
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
                .blur(blurRadius)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(16.dp)
            ) {
                content()
            }
        }

    }
}