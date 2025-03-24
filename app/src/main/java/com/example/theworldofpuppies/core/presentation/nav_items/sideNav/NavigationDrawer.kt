package com.example.theworldofpuppies.core.presentation.nav_items.sideNav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomAppbar
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavBar
import com.example.theworldofpuppies.core.presentation.nav_items.topNav.TopAppbar
import com.example.theworldofpuppies.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import okhttp3.internal.wait

@Composable
fun NavigationDrawer(
    scope: CoroutineScope,
    drawerState: DrawerState,
    content: @Composable (PaddingValues) -> Unit,
    bottomBarVisible: Boolean,
    navController: NavHostController,
    profileButtonVisible: Boolean,
    onSignOutClick: () -> Unit,
    topBarVisibility: Boolean,
    gesturesEnabled: Boolean
) {
    ModalNavigationDrawer(
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onSignOutClick = onSignOutClick
            )
        },
        drawerState = drawerState,
        scrimColor = Color.Transparent.copy(0.5f)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (topBarVisibility) {
                    TopAppbar(
                        navController = navController,
                        modifier = Modifier,
                        profileButtonVisibility = profileButtonVisible,
                        scope = scope,
                        drawerState = drawerState
                    )
                }
            },
            bottomBar = {
                if (bottomBarVisible) {
                    BottomAppbar(navController = navController)
//                    BottomNavBar()
                }
            },
            containerColor = Color.White
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                content(innerPadding)
            }
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavHostController,
    onSignOutClick: () -> Unit,
) {
    val drawerItems = listOf(
        DrawerItems.Refer,
        DrawerItems.Support,
        DrawerItems.Shop,
        DrawerItems.Insurance,
        DrawerItems.Training,
        DrawerItems.PrivacyPolicy,
        DrawerItems.TermsConditions,
        DrawerItems.RateUs,
        DrawerItems.SignOut(onSignOutClick)
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DrawerHeader()

            drawerItems.forEach { drawerItem ->
                DrawerItem(
                    title = drawerItem.title,
                    icon = drawerItem.icon,
                    onClick = {
                        when (drawerItem) {
                            is DrawerItems.SignOut -> drawerItem.onSignOutClick()
                            else -> navController.navigate(drawerItem.route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(Color.LightGray.copy(0.4f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pet Care", color = Color.Black)
    }
}

@Composable
fun DrawerItem(title: String, icon: ImageVector?, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text = title, fontSize = 15.sp) },
        selected = false,
        icon = {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = title,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            unselectedContainerColor = Color.White
        )
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        NavigationDrawer(
            scope = rememberCoroutineScope(),
            drawerState = DrawerState(initialValue = DrawerValue.Open),
            content = {},
            bottomBarVisible = true,
            navController = rememberNavController(),
            profileButtonVisible = true,
            onSignOutClick = {},
            topBarVisibility = true,
            gesturesEnabled = true
        )
    }
}