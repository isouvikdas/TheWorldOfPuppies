package com.example.theworldofpuppies.core.presentation.nav_items.topNav

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
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
    onBottomBarVisibilityChange: (Boolean) -> Unit
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
                containerColor = MaterialTheme.colorScheme.secondary.copy(0f),
                dividerColor = Color.LightGray
            ),
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { /* Handle search action */ },
            active = isSearchActive,
            onActiveChange = {
                isSearchActive = it
            },
            placeholder = { Text("Search products", color = Color.Gray) },
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
    } else {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(0.6f),
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
                        tint = MaterialTheme.colorScheme.primary
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
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                IconButton(onClick = { /* Bag action */ }) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = "Bag",
                        tint = MaterialTheme.colorScheme.primary
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
                            it.selectedIcon(
                                Modifier.size(MaterialTheme.dimens.small2),
                                MaterialTheme.colorScheme.tertiaryContainer
                            )
                        }
                    }
                }
            }
        )
    }
}


//@Preview
//@Composable
//fun TopAppBarPreview() {
//    AppTheme {
//        val topBarVisible by remember { mutableStateOf(true) }
//        val navController: NavHostController = rememberNavController()
//        Scaffold(
//            topBar = {
//                if (topBarVisible) {
//                    TopAppbar(
//                        navController,
//                        modifier = Modifier,
//                        profileButtonVisibility = true,
//                        scope = rememberCoroutineScope(),
//                        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//                    )
//                }
//            }
//        ) { paddingValues ->
//            Box(modifier = Modifier.padding(paddingValues))
//        }
//    }
//}
