package com.example.theworldofpuppies.address.presentation.util

import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.AddressType

fun AddressType.getIconRes(): Int = when (this) {
    AddressType.HOME -> R.drawable.home
    AddressType.OFFICE -> R.drawable.office_bag
    AddressType.OTHER -> R.drawable.other
    AddressType.NULL -> R.drawable.home
}