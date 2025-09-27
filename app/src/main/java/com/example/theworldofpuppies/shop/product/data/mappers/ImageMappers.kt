package com.example.theworldofpuppies.shop.product.data.mappers

import com.example.theworldofpuppies.shop.product.data.remote.dto.ImageDto
import com.example.theworldofpuppies.shop.product.domain.Image

fun ImageDto.toImage(): Image {
    return Image(
        fileName = fileName,
        fileType = fileType,
        fetchUrl = fetchUrl,
        s3Key = s3Key
    )
}