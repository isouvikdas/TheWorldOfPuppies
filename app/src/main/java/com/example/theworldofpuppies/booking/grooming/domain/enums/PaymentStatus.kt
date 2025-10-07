package com.example.theworldofpuppies.booking.grooming.domain.enums

import android.content.Context
import com.example.theworldofpuppies.R

enum class PaymentStatus {
    AWAITING_PAYMENT,
    PAID,
    FAILED,
    NUll
}

fun PaymentStatus.toString(context: Context): String {
    val resId = when(this) {
        PaymentStatus.AWAITING_PAYMENT -> R.string.awaiting_payment
        PaymentStatus.PAID -> R.string.paid
        PaymentStatus.FAILED -> R.string.failed
        PaymentStatus.NUll -> R.string.paid
    }
    return context.getString(resId)
}