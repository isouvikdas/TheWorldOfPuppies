package com.example.theworldofpuppies.core.presentation.nav_items.topNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TopNavigationItems(
    val route: String,
    val icon: ImageVector? = null
) {
    data object Cart: TopNavigationItems(
        route = "CartDto",
        icon = Icons.Outlined.ShoppingCart
    )

    data object Profile: TopNavigationItems(
        route = "Profile",
        icon = Icons.Outlined.AccountCircle
    )
}