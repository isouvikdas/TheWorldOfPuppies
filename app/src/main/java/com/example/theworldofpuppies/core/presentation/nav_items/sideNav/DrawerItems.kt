package com.example.theworldofpuppies.core.presentation.nav_items.sideNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.SafetyCheck
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerItems(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    data object Refer : DrawerItems(
        route = "Refer",
        title = "Refer & Earn",
        icon = Icons.Default.ManageAccounts
    )

    data object Support : DrawerItems(
        route = "Support",
        title = "Support",
        icon = Icons.Outlined.Phone
    )

    data object Shop : DrawerItems(
        route = "Shop",
        title = "Shop",
        icon = Icons.Outlined.ShoppingCart
    )

    data object Insurance : DrawerItems(
        route = "Pet insurance",
        title = "Pet Insurance",
        icon = Icons.Outlined.Pets
    )

    data object Training : DrawerItems(
        route = "Training",
        title = "Dog Training",
        icon = Icons.Outlined.Pets
    )

    data object PrivacyPolicy : DrawerItems(
        route = "PrivacyPolicy",
        title = "Privacy & Policy",
        icon = Icons.AutoMirrored.Outlined.Notes
    )

    data object TermsConditions : DrawerItems(
        route = "TermsConditions",
        title = "Terms & Conditions",
        icon = Icons.Outlined.SafetyCheck
    )

    data object RateUs : DrawerItems(
        route = "Rate Us",
        title = "Rate Us",
        icon = Icons.Outlined.StarOutline
    )

    data class SignOut(val onSignOutClick: () -> Unit) : DrawerItems(
        route = "SignOut",
        title = "Sign Out",
        icon = Icons.AutoMirrored.Outlined.Logout
    )
}

