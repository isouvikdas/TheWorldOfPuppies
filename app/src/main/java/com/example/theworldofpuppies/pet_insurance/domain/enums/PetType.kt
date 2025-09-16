package com.example.theworldofpuppies.pet_insurance.domain.enums

import android.content.Context
import com.example.theworldofpuppies.R

enum class PetType {
    DOG,
    CAT,
    OTHER
}

fun PetType.toString(context: Context): String {
    val resId = when (this) {
        PetType.DOG -> R.string.dog
        PetType.CAT -> R.string.cat
        PetType.OTHER -> R.string.other
    }
    return context.getString(resId)
}

