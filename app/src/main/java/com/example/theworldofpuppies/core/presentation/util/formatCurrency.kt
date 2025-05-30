package com.example.theworldofpuppies.core.presentation.util

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double, locale: Locale = Locale("en", "IN")): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
    return currencyFormatter.format(amount)
}