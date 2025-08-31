package com.example.theworldofpuppies.booking.presentation.pet_walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.navigation.Screen
import kotlinx.coroutines.launch

class BookingPetWalkViewModel(): ViewModel() {

    fun onAddressChangeClick(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(Screen.AddressScreen.route)
        }
    }
}