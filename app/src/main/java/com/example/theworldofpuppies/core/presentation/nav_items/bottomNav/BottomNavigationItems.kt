package com.example.theworldofpuppies.core.presentation.nav_items.bottomNav

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed class BottomNavigationItems(
    val route: String,
    val title: String? = null,
    val selectedIcon: @Composable (Modifier) -> Unit,
    val unselectedIcon: @Composable (Modifier) -> Unit
) {

    data object Home : BottomNavigationItems(
        route = "Home",
        title = "Home",
        selectedIcon = { modifier ->
            Icon(
                Icons.Filled.Home, contentDescription = null, modifier = modifier.then(
                    Modifier.size(
                        26.dp
                    )
                )
            )
        },
        unselectedIcon = { modifier ->
            Icon(
                Icons.Outlined.Home, contentDescription = null, modifier = modifier.then(
                    Modifier.size(
                        26.dp
                    )
                )
            )
        }
    )

    data object Booking : BottomNavigationItems(
        route = "Booking",
        title = "Booking",
        selectedIcon = { modifier ->
            Icon(
                Icons.AutoMirrored.Filled.Article,
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
            )
        },
        unselectedIcon = { modifier ->
            Icon(
                Icons.AutoMirrored.Outlined.Article,
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
            )
        }
    )

    data object Shop : BottomNavigationItems(
        route = "Shop",
        title = "Shop",
        selectedIcon = { modifier ->
            Icon(
                Icons.Filled.Store,
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
            )
        },
        unselectedIcon = { modifier ->
            Icon(
                Icons.Outlined.Store,
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
            )
        }
    )

    data object Messages : BottomNavigationItems(
        route = "Messages",
        title = "Messages",
        selectedIcon = { modifier ->
            Icon(
                Icons.AutoMirrored.Filled.Chat,
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
            )
        },
        unselectedIcon = { modifier ->
            Icon(
                Icons.AutoMirrored.Outlined.Chat,
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
            )
        }
    )

    data object Profile : BottomNavigationItems(
        route = "Profile",
        title = "Me",
        selectedIcon = { modifier ->
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                modifier = modifier.then(Modifier.size(28.dp)),
            )
        },
        unselectedIcon = { modifier ->
            Icon(
                Icons.Outlined.Person,
                contentDescription = null,
                modifier = modifier.then(Modifier.size(28.dp)),
            )
        }
    )

}