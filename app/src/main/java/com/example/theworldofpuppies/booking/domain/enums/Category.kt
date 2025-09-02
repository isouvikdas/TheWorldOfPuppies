package com.example.theworldofpuppies.booking.domain.enums

import android.content.Context
import com.example.theworldofpuppies.R

enum class Category {
    GROOMING,
    WALKING,
    VETERINARY,
    DOG_TRAINING,
    PET_INSURANCE
}

fun Category.toString(context: Context) : String{
    val resId = when(this) {
        Category.GROOMING -> R.string.grooming
        Category.WALKING -> R.string.walking
        Category.VETERINARY -> R.string.veterinary
        Category.DOG_TRAINING -> R.string.dog_training
        Category.PET_INSURANCE -> R.string.pet_insurance
    }
    return context.getString(resId)
}