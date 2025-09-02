package com.example.theworldofpuppies.services.pet_walking.domain.enums

import android.content.Context
import com.example.theworldofpuppies.R
import java.time.DayOfWeek

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

fun Days.toDayOfWeek(): DayOfWeek {
    return when (this) {
        Days.MONDAY -> DayOfWeek.MONDAY
        Days.TUESDAY -> DayOfWeek.TUESDAY
        Days.WEDNESDAY-> DayOfWeek.WEDNESDAY
        Days.THURSDAY -> DayOfWeek.THURSDAY
        Days.FRIDAY -> DayOfWeek.FRIDAY
        Days.SATURDAY -> DayOfWeek.SATURDAY
        Days.SUNDAY -> DayOfWeek.SUNDAY
    }
}