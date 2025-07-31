package com.example.theworldofpuppies.shop.order.domain

sealed class PaymentMethod {
    object ONLINE : PaymentMethod()
    object COD : PaymentMethod()
    object IDLE: PaymentMethod()
}