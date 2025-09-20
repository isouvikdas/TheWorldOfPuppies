package com.example.theworldofpuppies.booking.core.domain

import android.content.Context
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.navigation.Screen

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

fun Category.getImageRes() = when (this) {
    Category.GROOMING -> R.drawable.grooming_image
    Category.WALKING -> R.drawable.pet_walking_image
    Category.VETERINARY -> R.drawable.vet_image
    Category.DOG_TRAINING -> R.drawable.dog_training_image
    Category.PET_INSURANCE -> R.drawable.pet_insurance_image
}

fun Category.getScreenRoute() = when(this) {
    Category.GROOMING -> Screen.GroomingScreen.route
    Category.WALKING -> Screen.PetWalkingScreen.route
    Category.VETERINARY -> Screen.VetScreen.route
    Category.DOG_TRAINING -> Screen.DogTrainingScreen.route
    Category.PET_INSURANCE -> Screen.PetInsuranceScreen.route

}