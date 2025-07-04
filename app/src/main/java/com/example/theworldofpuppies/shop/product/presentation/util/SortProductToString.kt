package com.example.theworldofpuppies.shop.product.presentation.util

import android.content.Context
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.shop.product.domain.util.SortProduct

fun SortProduct.toString(context: Context): String {
    val resId = when (this) {
        SortProduct.LATEST -> R.string.sort_latest
        SortProduct.RECOMMENDED -> R.string.sort_recommended
        SortProduct.HIGHEST_RATED -> R.string.sort_highest_rated
        SortProduct.HIGH_TO_LOW -> R.string.sort_high_to_low
        SortProduct.LOW_TO_HIGH -> R.string.sort_low_to_high
    }
    return context.getString(resId)
}