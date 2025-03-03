package com.example.theworldofpuppies.core.presentation.nav_items.bottomNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CoPresent
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.CoPresent
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems(
    val route: String,
    val title: String? = null,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {

    data object Home: BottomNavigationItems(
        route = "Home",
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object WatchList: BottomNavigationItems(
        route = "Watchlist",
        title = "Watchlist",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )

    data object Cart: BottomNavigationItems(
        route = "Cart",
        title = "Cart",
        selectedIcon = Icons.Filled.ShoppingBag,
        unselectedIcon = Icons.Outlined.ShoppingBag

    )

    data object Profile: BottomNavigationItems(
        route = "Profile",
        title = "Profile",
        selectedIcon = Icons.Filled.CoPresent,
        unselectedIcon = Icons.Outlined.CoPresent
    )
}