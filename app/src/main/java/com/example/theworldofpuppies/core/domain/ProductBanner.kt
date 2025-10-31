package com.example.theworldofpuppies.core.domain

import com.example.theworldofpuppies.navigation.Screen

data class ProductBanner(
    val targetScreen: Screen,
    val image: Int,
    val category: String
)
