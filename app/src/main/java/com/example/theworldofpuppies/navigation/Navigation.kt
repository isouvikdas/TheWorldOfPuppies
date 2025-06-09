package com.example.theworldofpuppies.navigation

import android.util.Log
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.theworldofpuppies.auth.presentation.WelcomeScreen
import com.example.theworldofpuppies.auth.presentation.login.LoginScreen
import com.example.theworldofpuppies.auth.presentation.login.LoginViewModel
import com.example.theworldofpuppies.auth.presentation.register.RegisterScreen
import com.example.theworldofpuppies.auth.presentation.register.RegistrationViewModel
import com.example.theworldofpuppies.booking.presentation.BookingScreen
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.presentation.AuthViewModel
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.home.presentation.HomeScreen
import com.example.theworldofpuppies.messages.presentation.MessageScreen
import com.example.theworldofpuppies.profile.presentation.ProfileScreen
import com.example.theworldofpuppies.shop.cart.presentation.CartScreen
import com.example.theworldofpuppies.shop.cart.presentation.CartViewModel
import com.example.theworldofpuppies.shop.product.presentation.ProductViewModel
import com.example.theworldofpuppies.shop.product.presentation.ShopHomeScreen
import com.example.theworldofpuppies.shop.product.presentation.product_detail.ProductDetailScreen
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductListScreen
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    data object RegistrationScreen : Screen("RegistrationScreen")
    data object LoginScreen : Screen("LoginScreen")
    data object WelcomeScreen : Screen("WelcomeScreen")
    data object ProductDetailScreen : Screen("ProductDetailScreen")
    data object ProductListScreen : Screen("ProductList")
    data object CartScreen: Screen("CartScreen")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit,
    onProfileButtonVisibilityChanged: (Boolean) -> Unit,
    onSignOutDialogVisibilityChanged: (Boolean) -> Unit,
    onTopBarVisibilityChanged: (Boolean) -> Unit,
    authViewModel: AuthViewModel,
    isLoggedIn: Boolean,
    onGesturesChanged: (Boolean) -> Unit,
    searchIconVisibilityChanged: (Boolean) -> Unit
) {

    val registrationViewModel = koinViewModel<RegistrationViewModel>()
    val registrationUiState by registrationViewModel.registrationUiState.collectAsStateWithLifecycle()

    val loginViewModel = koinViewModel<LoginViewModel>()
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        registrationViewModel.resetState()
        loginViewModel.resetState()
    }

    val cartViewModel = koinViewModel<CartViewModel>()
    val cartUiState by cartViewModel.cartUiState.collectAsStateWithLifecycle()

    val startingRoute = when {
        isLoggedIn -> BottomNavigationItems.Home.route
        else -> Screen.WelcomeScreen.route
    }

    val productViewModel = koinViewModel<ProductViewModel>()
    val productListState by productViewModel.productListState.collectAsStateWithLifecycle()
    val categoryListState by productViewModel.categoryListState.collectAsStateWithLifecycle()
    val featuredProductListState by productViewModel.featuredProductListState.collectAsStateWithLifecycle()
    val productDetailState by productViewModel.productDetailState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = startingRoute,
        enterTransition = { slideInHorizontally { it } }
    ) {

        composable(route = Screen.WelcomeScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.LoginScreen.route) },
                onRegisterClick = { navController.navigate(Screen.RegistrationScreen.route) }
            )
        }

        composable(route = Screen.RegistrationScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            RegisterScreen(
                registrationViewModel = registrationViewModel,
                registrationUiState = registrationUiState,
                onVerify = {
                    navController.navigate(BottomNavigationItems.Home.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        popUpTo(Screen.RegistrationScreen.route) { inclusive = true }
                        popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.LoginScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            LoginScreen(
                loginUiState = loginUiState,
                loginViewModel = loginViewModel,
                onRegisterClick = {
                    navController.navigate(Screen.RegistrationScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                },
                onVerify = {
                    navController.navigate(BottomNavigationItems.Home.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        popUpTo(Screen.RegistrationScreen.route) { inclusive = true }
                        popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = BottomNavigationItems.Home.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(false)
            onGesturesChanged(true)
            HomeScreen()
        }

        composable(route = BottomNavigationItems.Booking.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(false)
            onGesturesChanged(true)
            BookingScreen()
        }

        composable(route = BottomNavigationItems.Shop.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(true)
            onGesturesChanged(true)
            ShopHomeScreen(
                productListState = productListState,
                categoryListState = categoryListState,
                featuredProductListState = featuredProductListState,
                productViewModel = productViewModel,
                onProductSelect = {
                    navController.navigate(Screen.ProductDetailScreen.route)
                },
                getCategories = { productViewModel.fetchCategories() },
                getProducts = { productViewModel.fetchNextPage() },
                getFeaturedProducts = { productViewModel.fetchFeaturedProducts() }
            )
        }

        composable(route = BottomNavigationItems.Messages.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(false)
            onGesturesChanged(true)
            MessageScreen()
        }

        composable(route = BottomNavigationItems.Profile.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(false)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(false)
            onGesturesChanged(true)
            ProfileScreen()
        }
        composable(route = Screen.ProductListScreen.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(true)
            onGesturesChanged(false)
            ProductListScreen()
        }
        composable(route = Screen.ProductDetailScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            ProductDetailScreen(
                productDetailState = productDetailState,
                onBack = {
                    navController.popBackStack()
                },
                onCartClick = {
                    navController.navigate(Screen.CartScreen.route)
                },
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }
        composable(route = Screen.CartScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            CartScreen(
                onBack = {
                    navController.popBackStack()
                },
                cartUiState = cartUiState,
                cartViewModel = cartViewModel
            )
        }
    }
}

private fun hideAllChrome(
    onBottomBar: (Boolean) -> Unit,
    onTopBar: (Boolean) -> Unit,
    onProfile: (Boolean) -> Unit,
    onGestures: (Boolean) -> Unit,
    onSearchIcon: (Boolean) -> Unit
) {
    onBottomBar(false)
    onTopBar(false)
    onProfile(false)
    onGestures(false)
    onSearchIcon(false)
}
