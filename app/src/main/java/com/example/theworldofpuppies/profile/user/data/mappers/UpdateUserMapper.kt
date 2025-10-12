package com.example.theworldofpuppies.profile.user.data.mappers

import com.example.theworldofpuppies.profile.user.data.dto.UpdateUserDto
import com.example.theworldofpuppies.profile.user.domain.UpdateUser
import com.example.theworldofpuppies.shop.product.data.mappers.toImage

fun UpdateUserDto.toUpdateUser(): UpdateUser {
    return UpdateUser(
        email = email,
        username = username,
        image = image,
        fetchUrl = fetchUrl
    )
}