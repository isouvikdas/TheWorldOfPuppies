package com.example.theworldofpuppies.core.presentation.nav_items.sideNav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomAppbar
import com.example.theworldofpuppies.core.presentation.nav_items.topNav.TopAppbar
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    gesturesEnabled: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    searchIconVisibility: Boolean,
    onBottomBarVisibilityChanged: (Boolean) -> Unit

) {
    ModalNavigationDrawer(
        modifier = Modifier,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onSignOutClick = onSignOutClick,
                drawerState = drawerState,
                scope = scope
            )
        },
        drawerState = drawerState,
        scrimColor = Color.Transparent.copy(0.5f)
    ) {

        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            topBar = {
                if (topBarVisibility) {
                    TopAppbar(
                        navController = navController,
                        modifier = Modifier,
                        profileButtonVisibility = profileButtonVisible,
                        scope = scope,
                        drawerState = drawerState,
                        scrollBehavior = scrollBehavior,
                        searchIconVisibility = searchIconVisibility,
                        onBottomBarVisibilityChange = onBottomBarVisibilityChanged
                    )
                }
            },
            bottomBar = {
                if (bottomBarVisible) {
                    BottomAppbar(navController = navController)
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
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val drawerItems = listOf(
        DrawerItems.Shop,
        DrawerItems.Insurance,
        DrawerItems.Training,
        DrawerItems.PrivacyPolicy,
        DrawerItems.TermsConditions,
        DrawerItems.Refer,
        DrawerItems.RateUs,
        DrawerItems.Support
    )

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.secondary,
        drawerShape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth(0.65f)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DrawerHeader()

                drawerItems.forEach { drawerItem ->
                    DrawerItem(
                        title = drawerItem.title,
                        icon = {
                            drawerItem.icon(
                                Modifier.size(MaterialTheme.dimens.small2),
                                MaterialTheme.colorScheme.tertiaryContainer
                            )
                        },
                        onClick = {
                            navController.navigate(drawerItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }

            DrawerItem(
                title = "Sign Out",
                icon = {
                    DrawerItems.SignOut(onSignOutClick).icon(
                        Modifier.size(24.dp),
                        MaterialTheme.colorScheme.tertiaryContainer
                    )
                },
                onClick = { onSignOutClick() },
                modifier = Modifier.padding(bottom = 16.dp),
                selected = true
            )
        }
    }
}

@Composable
fun DrawerHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary.copy(0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Pet Care", color = Color.Black)
        }
    }
}

@Composable
fun DrawerItem(
    title: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.tertiaryContainer
            )
        },
        selected = selected,
        icon = {
            icon()
        },
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.surface.copy(0.3f),
            unselectedContainerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier
    )
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview
//@Composable
//private fun Preview() {
//    AppTheme {
//        NavigationDrawer(
//            scope = rememberCoroutineScope(),
//            drawerState = DrawerState(initialValue = DrawerValue.Open),
//            content = {},
//            bottomBarVisible = true,
//            navController = rememberNavController(),
//            profileButtonVisible = true,
//            onSignOutClick = {},
//            topBarVisibility = true,
//            gesturesEnabled = true,
//            scrollBehavior = TopAppBarState()
//        )
//    }
//}