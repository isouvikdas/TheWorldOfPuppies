package com.example.theworldofpuppies.core.presentation.nav_items.bottomNav

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.R

sealed class BottomNavigationItems(
    val route: String,
    val title: String? = null,
    val selectedIcon: @Composable (Modifier, Color) -> Unit,
    val unselectedIcon: @Composable (Modifier, Color) -> Unit,
) {

    data object Home : BottomNavigationItems(
        route = "Home",
        title = "Home",
        selectedIcon = { modifier, tint ->
            Icon(
                painter = painterResource(R.drawable.home_filled),
                contentDescription = null,
                modifier = modifier.then(
                    Modifier.size(
                        26.dp
                    )
                ),
                tint = tint
            )
        },
        unselectedIcon = { modifier, tint ->
            Icon(
                painter = painterResource(R.drawable.home_outline),
                contentDescription = null,
                modifier = modifier.then(
                    Modifier.size(
                        26.dp
                    )
                ),
                tint = tint

            )
        }
    )

    data object Booking : BottomNavigationItems(
        route = "Booking",
        title = "Booking",
        selectedIcon = { modifier, tint ->
            Icon(
                painter = painterResource(R.drawable.note_filled),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
                tint = tint
            )
        },
        unselectedIcon = { modifier, tint ->
            Icon(
                painter = painterResource(R.drawable.note_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
                tint = tint
            )
        }
    )

    data object Shop : BottomNavigationItems(
        route = "Shop",
        title = "Shop",
        selectedIcon = { modifier, tint ->
            Icon(
                painter = painterResource(R.drawable.shop_filled),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
                tint = tint
            )
        },
        unselectedIcon = { modifier, tint ->
            Icon(
                painter = painterResource(R.drawable.shop_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(26.dp)),
                tint = tint
            )
        }
    )

    data object Profile : BottomNavigationItems(
        route = "Profile",
        title = "Me",
        selectedIcon = { modifier, tint ->
            Icon(
                painter = painterResource(R.drawable.user_filled),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(28.dp)),
                tint = tint
            )
        },
        unselectedIcon = { modifier, tint ->
            Icon(
                painter = painterResource(R.drawable.user_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(28.dp)),
                tint = tint

            )
        }
    )

}