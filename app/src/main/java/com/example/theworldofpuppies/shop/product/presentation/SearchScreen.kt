package com.example.theworldofpuppies.shop.product.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.nav_items.ProductSearchView
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.cart.presentation.CartViewModel
import com.example.theworldofpuppies.shop.product.domain.Category
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel,
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val searchUiState by productViewModel.searchUiState.collectAsStateWithLifecycle()
    val searchQuery = searchUiState.query
    val categoryListState by productViewModel.categoryListState.collectAsStateWithLifecycle()
    val categoryList =
        rememberSaveable(categoryListState.categoryList) { categoryListState.categoryList }
    val selectedCategory = rememberSaveable { mutableStateOf<Category?>(null) }

    val productList by remember(searchUiState.results, selectedCategory.value) {
        derivedStateOf {
            if (selectedCategory.value == null) {
                searchUiState.results
            } else {
                searchUiState.results.filter { it.categoryName == selectedCategory.value?.name }
            }
        }
    }


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                title = {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    ) {
                        items(categoryList) { category ->
                            val isSelected = selectedCategory.value == category
                            FilterChip(
                                onClick = {
                                    selectedCategory.value = if (isSelected) null else category
                                },
                                label = { Text(category.name) },
                                selected = isSelected,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .padding(end = MaterialTheme.dimens.small1.div(2))
                                    .animateItem()
                                    .height(35.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                ),
                                border = BorderStroke(1.0.dp, color = MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                            modifier = Modifier
                                .padding(horizontal = MaterialTheme.dimens.extraSmall)
                                .size(
                                    MaterialTheme.dimens.small1 + MaterialTheme.dimens.extraSmall.times(
                                        3
                                    )
                                )
                        ) {
                            Icon(
                                painterResource(R.drawable.arrow_left_filled),
                                contentDescription = "Menu",
                                modifier = Modifier
                                    .size(21.dp)
                            )
                        }
                        OutlinedTextField(
                            modifier = Modifier.width(MaterialTheme.dimens.medium2.times(4)).height(60.dp),
                            value = searchQuery,
                            onValueChange = { text -> productViewModel.onSearchTextChange(text) },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            placeholder = {
                                Text(
                                    "Search products",
                                    color = Color.Black.copy(0.9f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painterResource(R.drawable.search_dens),
                                    contentDescription = "Back",
                                    modifier = Modifier
                                        .size(21.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { productViewModel.onSearchTextChange("") }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear")
                                    }
                                }
                            },
                            shape = RoundedCornerShape(MaterialTheme.dimens.small2),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.LightGray.copy(0.4f),
                                unfocusedContainerColor = Color.LightGray.copy(0.4f)
                            )
                        )
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.CartScreen.route)
                            },
                            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall)
                        ) {
                            Icon(
                                painterResource(R.drawable.bag_outline),
                                contentDescription = "Bag",
                                modifier = Modifier.size(22.dp),
                            )
                        }

                    }
                }
            )


        },
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = Color.Transparent
            ) {
                ProductSearchView(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.dimens.extraSmall),
                    productViewModel = productViewModel,
                    color = Color.Transparent,
                    navController = navController,
                    productList = productList,
                    searchQuery = searchQuery,
                    isSearching = searchUiState.isSearching,
                    cartViewModel = cartViewModel
                )

            }
        }
    )

}
