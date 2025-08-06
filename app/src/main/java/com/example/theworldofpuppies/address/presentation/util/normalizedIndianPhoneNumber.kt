package com.example.theworldofpuppies.address.presentation.util

import com.rejowan.ccpc.Country

fun normalizeIndianPhoneNumber(input: String): String? {
    // Remove spaces, dashes, brackets etc., but keep the leading +
    val cleaned = input.trim().replace(Regex("[^\\d+]"), "")
        .replace(Regex("(?<!^)\\+"), "") // Remove any '+' not at the start


    return when {
        cleaned.startsWith(Country.India.countryCode) && cleaned.length == 13 -> cleaned

        cleaned.length == 10 && cleaned.all { it.isDigit() } && cleaned[0] in '6'..'9' ->
            Country.India.countryCode+cleaned

        else -> null // Invalid number
    }
}
