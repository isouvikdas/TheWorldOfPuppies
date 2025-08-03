package com.example.theworldofpuppies.core.presentation.util

fun formatPhoneNumber(phone: String): String {
    // Remove all spaces and non-digit/non-plus characters
    val cleaned = phone.replace("\\s+".toRegex(), "").replace("[^+0-9]".toRegex(), "")

    // Match pattern: +<countryCode><number>
    val regex = Regex("""^(\+\d{1,3})(\d{10})$""")
    val matchResult = regex.find(cleaned)

    return if (matchResult != null) {
        val countryCode = matchResult.groupValues[1]
        val number = matchResult.groupValues[2]

        // Split number into XXX-XXX-XXXX
        val formattedNumber = "${number.substring(0, 3)}-${number.substring(3, 6)}-${number.substring(6)}"

        "($countryCode) $formattedNumber"
    } else {
        phone // Return original if format doesn't match
    }
}