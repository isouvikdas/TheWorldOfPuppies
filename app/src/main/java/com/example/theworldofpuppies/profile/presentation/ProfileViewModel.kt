package com.example.theworldofpuppies.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val authEventManager: AuthEventManager
) : ViewModel() {

    fun onPetProfileClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.PetListScreen.route)
        }
    }

    fun onUserProfileClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.UpdateUserScreen.route)
        }
    }

    fun onAddressClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun onBookingClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(BottomNavigationItems.Booking.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun onOrderClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.OrderHistoryScreen.route)
        }
    }
}