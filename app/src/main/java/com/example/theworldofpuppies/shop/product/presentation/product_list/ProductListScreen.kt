package com.example.theworldofpuppies.shop.product.presentation.product_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.theworldofpuppies.shop.product.domain.Product
import com.example.theworldofpuppies.shop.product.domain.util.ListType
import com.example.theworldofpuppies.shop.product.domain.util.SortProduct
import com.example.theworldofpuppies.shop.product.presentation.ProductItem
import com.example.theworldofpuppies.shop.product.presentation.util.toString
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductListScreen(
    onProductSelect: (Product) -> Unit,
    isLoading: Boolean = false,
    onLoadMore: (() -> Unit)? = null,
    productTypeLabel: ListType,
    categoryListState: CategoryListState,
    productViewModel: ProductViewModel
) {
    val lazyGridState = rememberLazyGridState()
    val filteredSortedProducts by productViewModel.filteredSortedProducts.collectAsStateWithLifecycle(
        emptyList()
    )
    val selectedCategory by productViewModel.selectedCategory.collectAsStateWithLifecycle(null)
    val sortOption by productViewModel.sortOption.collectAsStateWithLifecycle()

    val categoryList =
        remember(categoryListState.categoryList) { categoryListState.categoryList }

    val sortOptions = listOf(
        SortProduct.RECOMMENDED,
        SortProduct.HIGHEST_RATED,
        SortProduct.HIGH_TO_LOW,
        SortProduct.LOW_TO_HIGH,
        SortProduct.LATEST
    )

    val context = LocalContext.current
    val isPaginationEnabled by productViewModel.isPaginationEnabled.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent
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
                            text = productTypeLabel.toString(context),
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
                            DropDownDemo(
                                sortOptions = sortOptions,
                                selectedIndex = sortOptions.indexOf(sortOption),
                                onOptionSelected = { index ->
                                    productViewModel.setSortOption(sortOptions[index])
                                },
                            )
                        }
                    }
                }
            }
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categoryList) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            onClick = {
                                productViewModel.setSelectedCategory(if (isSelected) null else category)
                            },
                            label = { Text(category.name) },
                            selected = isSelected,
                            shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                            modifier = Modifier
                                .padding(end = MaterialTheme.dimens.small1.div(2))
                                .animateItem(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(0.8f),
                                selectedLabelColor = Color.White
                            ),
                            border = BorderStroke(0.1.dp, color = Color.LightGray)
                        )
                    }
                }

            }
            items(filteredSortedProducts) { product ->
                ProductItem(
                    product = product,
                    onProductSelect = {
                        onProductSelect(product)
                    }
                )
            }
        }
    }

    if (isPaginationEnabled && onLoadMore != null) {
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
fun DropDownDemo(
    sortOptions: List<SortProduct>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                        text = sortOptions[selectedIndex].toString(context),
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
                sortOptions.forEachIndexed { index, filter ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = filter.toString(context),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        onClick = {
                            onOptionSelected(index)
                            isDropDownExpanded.value = false
                        }
                    )
                }

            }
        }
    }
}