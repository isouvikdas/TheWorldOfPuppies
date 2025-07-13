package com.example.theworldofpuppies.shop.product.presentation

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.nav_items.ProductSearchView
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    val searchUiState by productViewModel.searchUiState.collectAsStateWithLifecycle()
    val searchQuery = searchUiState.query
    var isSearchActive by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.dimens.extraSmall),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier
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
                },
                title = {


                },
                actions = {
                    OutlinedTextField(
                        modifier = Modifier.height(55.dp),
                        value = searchQuery,
                        onValueChange = { text -> productViewModel.onSearchTextChange(text) },
                        placeholder = { Text("Search products", color = Color.Black.copy(0.9f)) },
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
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            )

        },
        content = {
            ProductSearchView(
                modifier = Modifier
                    .padding(it),
                productViewModel = productViewModel,
                color = Color.Transparent,
                navController = navController
            )
        }
    )

}


//DockedSearchBar(
//colors = SearchBarDefaults.colors(
//containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
//dividerColor = Color.Transparent
//),
//query = searchQuery,
//onQueryChange = { text -> productViewModel.onSearchTextChange(text) },
//onSearch = { text -> productViewModel.onSearchTextChange(text) },
//active = isSearchActive,
//onActiveChange = { text ->
//    isSearchActive = text
//},
//placeholder = { Text("Search products", color = Color.Black.copy(0.9f)) },
//leadingIcon = {
//    Icon(
//        painterResource(R.drawable.search_dens),
//        contentDescription = "Back",
//        modifier = Modifier
//            .size(21.dp)
//    )
//
//},
//trailingIcon = {
//    if (searchQuery.isNotEmpty()) {
//        IconButton(onClick = { productViewModel.onSearchTextChange("") }) {
//            Icon(Icons.Default.Close, contentDescription = "Clear")
//        }
//    }
//},
//modifier = modifier
//.wrapContentHeight()
//
//) {
//
//}
//

