package com.example.theworldofpuppies.shop.product.presentation.category_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.product.domain.util.ListType
import com.example.theworldofpuppies.shop.product.presentation.CategoryItem
import com.example.theworldofpuppies.shop.product.presentation.product_list.CategoryListState
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    productViewModel: ProductViewModel,
    categoryListState: CategoryListState
) {

    val lazyGridState = rememberLazyGridState()

    val categories =
        categoryListState.categoryList + categoryListState.categoryList
    val isLoading = categoryListState.isLoading

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by productViewModel.isCategoryRefreshing.collectAsStateWithLifecycle()


    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { productViewModel.forceLoadCategoryScreen() },
            state = pullToRefreshState
        ) {
            if (categories.isNotEmpty() && !isLoading) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    state = lazyGridState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalArrangement = Arrangement.spacedBy(20.dp),

                    ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onCategoryClick = {
                                productViewModel.setSelectedCategory(category.name)
                                productViewModel.setListType(ListType.ALL)
                                navController.navigate(Screen.ProductListScreen.route)
                            }
                        )
                    }
                }
            }

        }
    }

}