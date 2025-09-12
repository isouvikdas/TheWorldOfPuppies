package com.example.theworldofpuppies.services.vet.domain

import android.content.Context
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.services.vet.domain.enums.VetBookingCategory
import kotlinx.serialization.Serializable

@Serializable
data class VetOption(
    val id: String,
    val category: VetBookingCategory,
    val description: String,
    val price: Double
)

fun VetBookingCategory.toString(context: Context): String {
    val resId = when (this) {
        VetBookingCategory.IN_CALL_BOOKING -> R.string.in_call_booking
        VetBookingCategory.HOME_VISIT -> R.string.home_visit
    }
    return context.getString(resId)
}

fun VetBookingCategory.getIconRes(): Int = when (this) {
    VetBookingCategory.IN_CALL_BOOKING -> R.drawable.vet_call
    VetBookingCategory.HOME_VISIT -> R.drawable.vet_visit
}