package com.example.theworldofpuppies.core.presentation.nav_items.sideNav

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.navigation.Screen

sealed class DrawerItems(
    val route: String,
    val title: String,
    val icon: @Composable (Modifier) -> Unit
) {

    data object Refer : DrawerItems(
        route = "Refer",
        title = "Refer & Earn",
        icon = { modifier ->
            Icon(
                painterResource(id = R.drawable.refer_earn_filled),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(20.dp))
            )
        }
    )
    data object Grooming : DrawerItems(
        route = Screen.GroomingScreen.route,
        title = "Grooming",
        icon = { modifier ->
            Icon(
                painterResource(id = R.drawable.refer_earn_filled),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(20.dp))
            )
        }
    )
    data object Booking : DrawerItems(
        route = Screen.BookingScreen.route,
        title = "Booking",
        icon = { modifier ->
            Icon(
                painterResource(id = R.drawable.refer_earn_filled),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(20.dp))
            )
        }
    )

    data object Support : DrawerItems(
        route = "Support",
        title = "Support",
        icon = { modifier->
            Icon(
                painterResource(R.drawable.call_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(20.dp))
            )
        }
    )

    data object Shop : DrawerItems(
        route = "Shop",
        title = "Shop",
        icon = { modifier->
            Icon(
                painterResource(R.drawable.shop_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object Insurance : DrawerItems(
        route = "Pet insurance",
        title = "Pet Insurance",
        icon = { modifier->
            Icon(
                painterResource(R.drawable.pet_insurance_filled),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object Training : DrawerItems(
        route = "Training",
        title = "Dog Training",
        icon = { modifier->
            Icon(
                painterResource(id = R.drawable.dog_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object PrivacyPolicy : DrawerItems(
        route = "PrivacyPolicy",
        title = "Privacy & Policy",
        icon = { modifier->
            Icon(
                painterResource(R.drawable.privacy_pollicy_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object TermsConditions : DrawerItems(
        route = "TermsConditions",
        title = "T&C",
        icon = { modifier->
            Icon(
                painterResource(R.drawable.terms_conditions_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object RateUs : DrawerItems(
        route = "Rate Us",
        title = "Rate Us",
        icon = { modifier->
            Icon(
                painterResource(R.drawable.star_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data class SignOut(val onSignOutClick: () -> Unit) : DrawerItems(
        route = "SignOut",
        title = "Sign Out",
        icon = { modifier->
            Icon(
                painterResource(R.drawable.logout_outline),
                contentDescription = null,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )
}

