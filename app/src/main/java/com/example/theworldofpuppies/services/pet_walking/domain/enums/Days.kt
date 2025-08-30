package com.example.theworldofpuppies.services.pet_walking.domain.enums

import android.content.Context
import com.example.theworldofpuppies.R

enum class Days {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

fun Days.toString(context: Context): String {
    val resId = when (this) {
        Days.MONDAY -> R.string.mon
        Days.TUESDAY -> R.string.tue
        Days.WEDNESDAY -> R.string.wed
        Days.THURSDAY -> R.string.thu
        Days.FRIDAY -> R.string.fri
        Days.SATURDAY -> R.string.sat
        Days.SUNDAY -> R.string.sun
    }
    return context.getString(resId)
}