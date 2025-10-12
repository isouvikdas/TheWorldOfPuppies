package com.example.theworldofpuppies.profile.user.domain

import android.net.Uri
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface UpdateUserRepository {
    suspend fun updateUser(
        imageUri: Uri?,
        username: String?,
        email: String?,
    ): Result<UpdateUser, NetworkError>
}