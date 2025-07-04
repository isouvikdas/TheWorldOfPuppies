package com.example.theworldofpuppies.shop.product.presentation.util

import android.content.Context
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.shop.product.domain.util.ListType

fun ListType.toString(context: Context): String {
    val resId = when (this) {
        ListType.ALL -> R.string.all_products
        ListType.POPULAR -> R.string.all_products
        ListType.NEW -> R.string.new_products
        ListType.FEATURED -> R.string.featured_products
    }
    return context.getString(resId)
}
