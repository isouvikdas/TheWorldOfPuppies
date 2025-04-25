package com.example.theworldofpuppies.core.presentation.nav_items.sideNav

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.R

sealed class DrawerItems(
    val route: String,
    val title: String,
    val icon: @Composable (Modifier, Color) -> Unit
) {
    data object Refer : DrawerItems(
        route = "Refer",
        title = "Refer & Earn",
        icon = { modifier, tint ->
            Icon(
                painterResource(id = R.drawable.credit_card_health),
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object Support : DrawerItems(
        route = "Support",
        title = "Support",
        icon = { modifier, tint ->
            Icon(
                Icons.Outlined.Phone,
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object Shop : DrawerItems(
        route = "Shop",
        title = "Shop",
        icon = { modifier, tint ->
            Icon(
                Icons.Filled.Store,
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object Insurance : DrawerItems(
        route = "Pet insurance",
        title = "Pet Insurance",
        icon = { modifier, tint ->
            Icon(
                Icons.Outlined.Pets,
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object Training : DrawerItems(
        route = "Training",
        title = "Dog Training",
        icon = { modifier, tint ->
            Icon(
                painterResource(id = R.drawable.sound_detection_dog_barking),
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object PrivacyPolicy : DrawerItems(
        route = "PrivacyPolicy",
        title = "Privacy & Policy",
        icon = { modifier, tint ->
            Icon(
                Icons.AutoMirrored.Outlined.EventNote,
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object TermsConditions : DrawerItems(
        route = "TermsConditions",
        title = "T&C",
        icon = { modifier, tint ->
            Icon(
                Icons.Outlined.Description,
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data object RateUs : DrawerItems(
        route = "Rate Us",
        title = "Rate Us",
        icon = { modifier, tint ->
            Icon(
                Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )

    data class SignOut(val onSignOutClick: () -> Unit) : DrawerItems(
        route = "SignOut",
        title = "Sign Out",
        icon = { modifier, tint ->
            Icon(
                Icons.AutoMirrored.Outlined.Logout,
                contentDescription = null,
                tint = tint,
                modifier = modifier.then(Modifier.size(27.dp))
            )
        }
    )
}

