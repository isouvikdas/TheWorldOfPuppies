package com.example.theworldofpuppies.shop.product.data.mappers

import com.example.theworldofpuppies.shop.product.data.local.ProductEntity
import com.example.theworldofpuppies.shop.product.data.remote.dto.ProductDto
import com.example.theworldofpuppies.shop.product.domain.Product

fun ProductDto.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        price = price,
        discount = discount,
        discountedPrice = discountedPrice,
        description = description,
        categoryName = categoryName,
        inventory = inventory,
        firstImageId = firstImageId,
        imageIds = imageIds,
        isFeatured = isFeatured,
        isRecommended = isRecommended,
        rating = rating
    )
}

fun ProductEntity.toProduct(): Product {
    return Product(
        localId = localId,
        id = id,
        name = name,
        price = price,
        discountedPrice = discountedPrice,
        discount = discount,
        description = description,
        categoryName = categoryName,
        inventory = inventory,
        firstImageId = firstImageId,
        imageIds = imageIds,
        firstImageUri = this.firstImageUri,
        isFeatured = isFeatured,
        isRecommended = isRecommended,
        rating = rating
    )
}