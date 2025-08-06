package com.example.theworldofpuppies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.theworldofpuppies.auth.presentation.signOut.SignOutDialog
import com.example.theworldofpuppies.core.presentation.AuthViewModel
import com.example.theworldofpuppies.core.presentation.nav_items.sideNav.NavigationDrawer
import com.example.theworldofpuppies.navigation.AppNavigation
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val systemUiController = rememberSystemUiController()

            SideEffect {
                systemUiController.setNavigationBarColor(
                    color = Color.Black,
                    darkIcons = false
                )
            }

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


                    var bottomBarVisible by remember { mutableStateOf(true) }
                    var profileButtonVisible by remember { mutableStateOf(true) }
                    var topBarVisible by remember {
                        mutableStateOf(true)
                    }
                    var gesturesEnabled by remember {
                        mutableStateOf(true)
                    }

                    var searchIconVisibility by remember {
                        mutableStateOf(false)
                    }
                    var searchScreenVisibility by remember {
                        mutableStateOf(false)
                    }
                    var openSignOutDialog by remember { mutableStateOf(false) }

                    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
                        state = rememberTopAppBarState()
                    )

                    NavigationDrawer(
                        scope = scope,
                        drawerState = drawerState,
                        navController = navController,
                        bottomBarVisible = bottomBarVisible,
                        profileButtonVisible = profileButtonVisible,
                        onSignOutClick = { openSignOutDialog = true },
                        topBarVisibility = topBarVisible,
                        gesturesEnabled = gesturesEnabled,
                        modifier = Modifier,
                        searchIconVisibility = searchIconVisibility,
                        onBottomBarVisibilityChanged = { bottomBarVisible = it },
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
                                    onGesturesChanged = { gesturesEnabled = it },
                                    searchIconVisibilityChanged = { searchIconVisibility = it }
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
                        },
                        scrollBehavior = scrollBehavior
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
