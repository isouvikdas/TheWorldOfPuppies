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
import androidx.compose.runtime.collectAsState
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
import com.example.theworldofpuppies.auth.presentation.login.LoginViewModel
import com.example.theworldofpuppies.auth.presentation.signOut.SignOutDialog
import com.example.theworldofpuppies.core.presentation.AuthViewModel
import com.example.theworldofpuppies.core.presentation.nav_items.sideNav.NavigationDrawer
import com.example.theworldofpuppies.navigation.AppNavigation
import com.example.theworldofpuppies.shop.order.presentation.OrderViewModel
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    private lateinit var orderViewModel: OrderViewModel
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
                orderViewModel = koinViewModel<OrderViewModel>()
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
                                    searchIconVisibilityChanged = { searchIconVisibility = it },
                                    orderViewModel = orderViewModel
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


    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData) {
        if (!razorpayPaymentId.isNullOrEmpty()) {
            orderViewModel.verifyPayment(
                razorpayOrderId = paymentData.orderId,
                signature = paymentData.signature,
                paymentId = razorpayPaymentId,
            )
        }
    }
    override fun onPaymentError(code: Int, message: String?, p2: PaymentData?) {
        orderViewModel.handlePaymentError(code, message ?: "")
    }
}

