package com.example.theworldofpuppies.core.presentation.nav_items.topNav

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.nav_items.ProductSearchView
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppbar(
    navController: NavHostController,
    modifier: Modifier,
    profileButtonVisibility: Boolean,
    scope: CoroutineScope,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior,
    searchIconVisibility: Boolean,
    onSearchClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                    }
                }
            ) {
                Icon(
                    painterResource(R.drawable.menu_flipped),
                    contentDescription = "Menu",
                    modifier = Modifier
                        .size(22.dp)
                )
            }
        },
        title = {
            Text(
                text = "The World of Puppies",
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            if (searchIconVisibility) {
                IconButton(
                    onClick = {
                        onSearchClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.search_dens),
                        contentDescription = "Search",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            IconButton(onClick = { navController.navigate(Screen.CartScreen.route) }) {
                Icon(
                    painter = painterResource(R.drawable.bag_outline),
                    contentDescription = "Bag",
                    modifier = Modifier.size(22.dp)
                )
            }
            if (profileButtonVisibility) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                BottomNavigationItems.Profile.let {
                    IconButton(onClick = {
                        if (currentRoute != it.route) {
                            navController.navigate(it.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.user_outline),
                            contentDescription = "Profile",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    )

}
