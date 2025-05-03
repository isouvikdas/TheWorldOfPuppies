package com.example.theworldofpuppies.navigation

import android.content.Context
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
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
import com.example.theworldofpuppies.core.presentation.AuthViewModel
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.home.presentation.HomeScreen
import com.example.theworldofpuppies.messages.presentation.MessageScreen
import com.example.theworldofpuppies.profile.presentation.ProfileScreen
import com.example.theworldofpuppies.shop.product.presentation.ProductViewModel
import com.example.theworldofpuppies.shop.product.presentation.ShopHomeScreen
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    data object RegistrationScreen : Screen("RegistrationScreen")
    data object LoginScreen : Screen("LoginScreen")
    data object WelcomeScreen : Screen("WelcomeScreen")
    data object HomeScreen : Screen("HomeScreen")
    data object ProductList : Screen("ProductList")
    data object ProductDetail : Screen("ProductDetail")
    data object AccountDetail : Screen("AccountDetail")
    data object Address : Screen("Address")
    data object MyOrders : Screen("MyOrders")
    data object UpdateEmail : Screen("UpdateEmail")
    data object ContactInfo : Screen("ContactInfo")
    data object UpdateUsername : Screen("UpdateUsername")
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
    registrationViewModel.resetState()

    val loginViewModel = koinViewModel<LoginViewModel>()
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()
    loginViewModel.resetState()

    val startingRoute = when {
        isLoggedIn -> BottomNavigationItems.Home.route
        else -> Screen.WelcomeScreen.route
    }

    val productViewModel = koinViewModel<ProductViewModel>()
    val productListState by productViewModel.productListState.collectAsStateWithLifecycle()
    val categoryListState by productViewModel.categoryListState.collectAsStateWithLifecycle()
    val featuredProductListState by productViewModel.featuredProductListState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = startingRoute,
        enterTransition = { slideInHorizontally { it } }
    ) {

        composable(route = Screen.WelcomeScreen.route) {
            onBottomBarVisibilityChanged(false)
            onTopBarVisibilityChanged(false)
            onGesturesChanged(false)
            searchIconVisibilityChanged(false)
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.LoginScreen.route) },
                onRegisterClick = { navController.navigate(Screen.RegistrationScreen.route) }
            )
        }

        composable(route = Screen.RegistrationScreen.route) {
            onBottomBarVisibilityChanged(false)
            onTopBarVisibilityChanged(false)
            onGesturesChanged(false)
            searchIconVisibilityChanged(false)
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
            onBottomBarVisibilityChanged(false)
            onTopBarVisibilityChanged(false)
            searchIconVisibilityChanged(false)
            onGesturesChanged(false)
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
                onProductSelect = {},
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
    }
}

