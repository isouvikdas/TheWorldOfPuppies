package com.example.theworldofpuppies.core.presentation.nav_items.topNav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingBag
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.navigation.Screen
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
    onBottomBarVisibilityChange: (Boolean) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    if (isSearchActive) {
        onBottomBarVisibilityChange(false)
    } else {
        onBottomBarVisibilityChange(true)
    }

    if (isSearchActive) {
        SearchBar(
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.4f),
                dividerColor = Color.Transparent
            ),
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { /* Handle search action */ },
            active = isSearchActive,
            onActiveChange = {
                isSearchActive = it
            },
            placeholder = { Text("Search products", color = Color.Black.copy(0.9f)) },
            leadingIcon = {
                IconButton(onClick = {
                    isSearchActive = false
                }) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
            ) {
                Text(
                    "Suggestion 1",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.dimens.small2,
                        vertical = MaterialTheme.dimens.small1
                    )
                )
                Text(
                    "Suggestion 2",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.dimens.small2,
                        vertical = MaterialTheme.dimens.small1
                    )
                )

            }
        }
    } else {
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
                        Icons.AutoMirrored.Default.Sort,
                        contentDescription = "Menu",
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
                            isSearchActive = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.search),
//                            Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(MaterialTheme.dimens.extraSmall.times(4))
                        )
                    }
                }
                IconButton(onClick = { navController.navigate(Screen.CartScreen.route) }) {
                    Icon(
                        Icons.Outlined.ShoppingBag,
                        contentDescription = "Bag",
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
                            it.unselectedIcon(
                                Modifier.size(MaterialTheme.dimens.small2),
                            )
                        }
                    }
                }
            }
        )
    }
}
