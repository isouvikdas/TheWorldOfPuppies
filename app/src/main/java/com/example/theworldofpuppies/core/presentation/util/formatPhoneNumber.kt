package com.example.theworldofpuppies.core.presentation.util

import com.rejowan.ccpc.Country

fun formatPhoneNumber(phone: String): String {
    // Remove spaces and non-digit/non-plus characters

    val cleaned = addCountryCode(phone)
    // Match pattern: +<countryCode><number>
    val regex = Regex("""^(\+\d{1,3})(\d{10})$""")
    val matchResult = regex.find(cleaned)

    return if (matchResult != null) {
        val countryCode = matchResult.groupValues[1]
        val number = matchResult.groupValues[2]

        // Format: XXX-XXX-XXXX
        val formattedNumber = "${number.substring(0, 3)}-${number.substring(3, 6)}-${number.substring(6)}"

        "($countryCode) $formattedNumber"
    } else {
        phone // Return original if pattern doesn't match
    }
}

fun addCountryCode(phone: String): String {
    var cleaned = phone.replace("\\s+".toRegex(), "").replace("[^+0-9]".toRegex(), "")

    // If number doesn't start with '+', assume it's an Indian number and add +91
    if (!cleaned.startsWith("+")) {
        // Ensure only 10-digit number exists before appending
        val digitsOnly = cleaned.replace("[^0-9]".toRegex(), "")
        if (digitsOnly.length == 10) {
            cleaned = Country.India.countryCode + digitsOnly
        } else {
            // Invalid format, return original
            return phone
        }
    }
    return cleaned
}
