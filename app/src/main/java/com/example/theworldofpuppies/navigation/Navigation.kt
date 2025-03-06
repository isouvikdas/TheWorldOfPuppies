package com.example.theworldofpuppies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.theworldofpuppies.auth.presentation.login.LoginScreen
import com.example.theworldofpuppies.auth.presentation.login.LoginViewModel
import com.example.theworldofpuppies.auth.presentation.register.RegisterScreen
import com.example.theworldofpuppies.auth.presentation.register.RegistrationViewModel
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    data object RegistrationScreen : Screen("RegistrationScreen")
    data object LoginScreen: Screen("LoginScreen")
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
    navController: NavHostController
) {
    val registrationViewModel = koinViewModel<RegistrationViewModel>()
    val registrationUiState by registrationViewModel.registrationUiState.collectAsStateWithLifecycle()

    val loginViewModel = koinViewModel<LoginViewModel>()
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.RegistrationScreen.route
    ) {
        composable(route = Screen.RegistrationScreen.route) {
            RegisterScreen(
                registrationViewModel = registrationViewModel,
                registrationUiState = registrationUiState
            )
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                loginUiState = loginUiState,
                loginViewModel = loginViewModel
            )
        }
    }
}