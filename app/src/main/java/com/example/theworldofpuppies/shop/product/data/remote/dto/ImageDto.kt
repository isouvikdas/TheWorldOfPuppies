package com.example.theworldofpuppies.shop.product.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val fileName: String,
    val fileType: String,
    val fetchUrl: String,
    val s3Key: String,
)