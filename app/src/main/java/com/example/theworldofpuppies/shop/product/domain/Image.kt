package com.example.theworldofpuppies.shop.product.domain

data class Image(
    val fileName: String,
    val fileType: String,
    val fetchUrl: String,
    val s3Key: String
)
