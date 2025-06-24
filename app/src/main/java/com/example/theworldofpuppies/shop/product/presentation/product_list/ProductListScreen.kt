package com.example.theworldofpuppies.shop.product.presentation.product_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.shop.product.domain.Category
import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.shop.product.presentation.ProductItem
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductListScreen(
    productList: List<Product>,
    onProductSelect: (Product) -> Unit,
    enablePagination: Boolean = false,
    isLoading: Boolean = false,
    onLoadMore: (() -> Unit)? = null,
    productTypeLabel: String = "",
    categoryListState: CategoryListState
) {
    val lazyGridState = rememberLazyGridState()
    val categoryList =
        remember(categoryListState.categoryList) { categoryListState.categoryList }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
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
                Column {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = productTypeLabel,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = MaterialTheme.dimens.extraSmall)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Sort By:",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically),
                                fontWeight = FontWeight.W500
                            )
                            DropDownDemo()
                        }
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categoryList.forEach { category ->
                            val isSelected = selectedCategory == category
                            FilterChip(
                                onClick = {
                                    selectedCategory = if (isSelected) null else category
                                },
                                label = { Text(category.name) },
                                selected = isSelected,
                                shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                                modifier = Modifier.padding(end = 8.dp).animateItem(),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                                        0.3f
                                    ),
                                    selectedLabelColor = Color.Black
                                ),
                                border = BorderStroke(0.1.dp, color = Color.LightGray)
                            )
                        }
                    }

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
    val usernames = listOf("Recommended", "High to Low", "Low to High", "Highest Rated", "Latest")

    Column(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.dimens.extraSmall)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.End
    ) {
        Box(modifier = Modifier.width(IntrinsicSize.Max)) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.clickable { isDropDownExpanded.value = true }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.small1.div(4).times(5)
                    )
                ) {
                    Text(
                        text = usernames[itemPosition.intValue],
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = if (isDropDownExpanded.value) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 0.6.dp
                )
            }
        }

        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false },
            modifier = Modifier
                .background(Color.White)
                .padding(top = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                usernames.forEachIndexed { index, username ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = username,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
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

@Composable
fun CategoryList(
    cateGory: (String) -> Unit,

    ) {

}