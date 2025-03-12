package com.example.theworldofpuppies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.theworldofpuppies.auth.presentation.WelcomeScreen
import com.example.theworldofpuppies.auth.presentation.register.RegisterScreen
import com.example.theworldofpuppies.auth.presentation.signOut.SignOutDialog
import com.example.theworldofpuppies.core.presentation.AuthViewModel
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomAppbar
import com.example.theworldofpuppies.core.presentation.nav_items.sideNav.NavigationDrawer
import com.example.theworldofpuppies.core.presentation.nav_items.topNav.TopAppbar
import com.example.theworldofpuppies.navigation.AppNavigation
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {

                val authViewModel = koinViewModel<AuthViewModel>()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
                val resetKey by authViewModel.resetKey.collectAsStateWithLifecycle()

                key(resetKey) {
                    val navController = rememberNavController()
                    val currentRoute =
                        navController.currentBackStackEntryAsState().value?.destination?.route

                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    val statusBarColor = when (currentRoute) {
                        Screen.ProductDetail.route -> Color.LightGray.copy(0.1f)
                        else -> Color.White
                    }
                    val systemUiController = rememberSystemUiController()
                    SideEffect {
                        systemUiController.setStatusBarColor(
                            color = statusBarColor,
                            darkIcons = true
                        )
                    }


                    var bottomBarVisible by remember { mutableStateOf(true) }
                    var profileButtonVisible by remember { mutableStateOf(true) }
                    var topBarVisible by remember {
                        mutableStateOf(true)
                    }
                    var gesturesEnabled by remember {
                        mutableStateOf(true)
                    }
                    var openSignOutDialog by remember { mutableStateOf(false) }


                    NavigationDrawer(
                        scope = scope,
                        drawerState = drawerState,
                        navController = navController,
                        bottomBarVisible = bottomBarVisible,
                        profileButtonVisible = profileButtonVisible,
                        onSignOutClick = { openSignOutDialog = true },
                        topBarVisibility = topBarVisible,
                        gesturesEnabled = gesturesEnabled,
                        content = { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                AppNavigation(
                                    navController = navController,
                                    onBottomBarVisibilityChanged = { bottomBarVisible = it },
                                    onProfileButtonVisibilityChanged = {
                                        profileButtonVisible = it
                                    },
                                    authViewModel = authViewModel,
                                    onSignOutDialogVisibilityChanged = { openSignOutDialog = it },
                                    onTopBarVisibilityChanged = { topBarVisible = it },
                                    isLoggedIn = isLoggedIn,
                                    onGesturesChanged = { gesturesEnabled = it }
                                )

                                if (openSignOutDialog) {
                                    SignOutDialog(
                                        onDismiss = { openSignOutDialog = false },
                                        onSignOutConfirm = {
                                            authViewModel.signOut()
                                            openSignOutDialog = false
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        })
                                }

                            }
                        }
                    )
                }


            }

        }
    }
}

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = true)
    }
}
