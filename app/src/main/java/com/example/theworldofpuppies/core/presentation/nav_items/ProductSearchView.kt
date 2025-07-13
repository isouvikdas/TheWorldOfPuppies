package com.example.theworldofpuppies.core.presentation.nav_items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.product.presentation.ProductItem
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun ProductSearchView(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel,
    color: Color,
    navController: NavController
) {
    val lazyGridState = rememberLazyGridState()

    val searchUiState by productViewModel.searchUiState.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = color
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = lazyGridState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.extraSmall),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.extraSmall)
        ) {
            items(searchUiState.results) { product ->
                ProductItem(
                    product = product,
                    onProductSelect = {
                        productViewModel.setProduct(product)
                        navController.navigate(Screen.ProductDetailScreen.route)
                    }
                )
            }
        }

    }

}