package com.example.theworldofpuppies.services.pet_walking.domain.enums

import android.content.Context
import com.example.theworldofpuppies.R

enum class Frequency {
    ONE_TIME,
    REPEAT_WEEKLY
}

fun Frequency.toString(context: Context): String {
    val resId = when (this) {
        Frequency.ONE_TIME -> R.string.one_time
        Frequency.REPEAT_WEEKLY -> R.string.repeat_weakly
    }
    return context.getString(resId)
}

fun Frequency.getIconRes(): Int = when (this) {
    Frequency.ONE_TIME -> R.drawable.once
    Frequency.REPEAT_WEEKLY -> R.drawable.repeat
}