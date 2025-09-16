package com.example.theworldofpuppies.core.presentation.util

fun calculateDiscountedPrice(discount: Int, price: Double) =
    price.times(100 - discount).div(100)
