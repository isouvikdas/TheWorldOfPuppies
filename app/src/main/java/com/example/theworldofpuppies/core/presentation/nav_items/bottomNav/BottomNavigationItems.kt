package com.example.theworldofpuppies.core.presentation.nav_items.bottomNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BookOnline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Textsms
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

    data object Booking: BottomNavigationItems(
        route = "Booking",
        title = "Booking",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook
    )

    data object Messages: BottomNavigationItems(
        route = "Messages",
        title = "Messages",
        selectedIcon = Icons.Filled.Textsms,
        unselectedIcon = Icons.Outlined.Textsms

    )

    data object Profile: BottomNavigationItems(
        route = "Profile",
        title = "Me",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )
}