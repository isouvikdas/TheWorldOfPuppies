package com.example.theworldofpuppies.profile.pet.presentation.utils

import android.content.Context
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender

fun Aggression.toString(context: Context): String {
    val resId = when (this) {
        Aggression.LOW -> R.string.low
        Aggression.MEDIUM -> R.string.medium
        Aggression.HIGH -> R.string.high
    }
    return context.getString(resId)
}

fun Gender.toString(context: Context): String {
    val resId = when (this) {
        Gender.FEMALE -> R.string.female
        Gender.MALE -> R.string.male
    }
    return context.getString(resId)
}