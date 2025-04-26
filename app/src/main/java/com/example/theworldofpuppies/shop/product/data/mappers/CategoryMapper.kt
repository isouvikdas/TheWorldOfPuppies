package com.example.theworldofpuppies.shop.product.data.mappers

import com.example.theworldofpuppies.shop.product.data.local.CategoryEntity
import com.example.theworldofpuppies.shop.product.data.remote.dto.CategoryDto
import com.example.theworldofpuppies.shop.product.domain.Category

fun CategoryDto.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        productIds = productIds
    )
}

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        productIds = productIds,
        localId = localId
    )
}