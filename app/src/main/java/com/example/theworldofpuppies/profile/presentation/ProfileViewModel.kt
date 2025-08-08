package com.example.theworldofpuppies.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.navigation.Screen
import kotlinx.coroutines.launch

class ProfileViewModel() : ViewModel() {

    fun onAddressClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }

    fun onMessageClick(navController: NavController) {
        viewModelScope.apply {
            navController.navigate(BottomNavigationItems.Messages.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
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