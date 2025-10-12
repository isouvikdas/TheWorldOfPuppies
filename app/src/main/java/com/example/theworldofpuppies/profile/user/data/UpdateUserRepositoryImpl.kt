package com.example.theworldofpuppies.profile.user.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.getBytes
import com.example.theworldofpuppies.core.presentation.util.getFileName
import com.example.theworldofpuppies.core.presentation.util.getMimeType
import com.example.theworldofpuppies.profile.user.data.mappers.toUpdateUser
import com.example.theworldofpuppies.profile.user.data.request.UpdateUserRequest
import com.example.theworldofpuppies.profile.user.domain.UpdateUser
import com.example.theworldofpuppies.profile.user.domain.UpdateUserRepository

class UpdateUserRepositoryImpl(
    private val userRepository: UserRepository,
    private val updateUserApi: UpdateUserApi,
    private val context: Context
) : UpdateUserRepository {

    override suspend fun updateUser(
        imageUri: Uri?,
        username: String?,
        email: String?,
    ): Result<UpdateUser, NetworkError> {
        Log.i("pet", "reached udpateUser1")
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        Log.i("pet", "reached udpateUse2")


        val bytes = if (imageUri != null && imageUri != Uri.EMPTY) {
            getBytes(imageUri, context)
                ?: return Result.Error(NetworkError.UNKNOWN)
        } else {
            null
        }
        Log.i("pet", "reached udpateUse3")
        val fileName = if (imageUri != null && imageUri != Uri.EMPTY) {
            getFileName(imageUri, context)
                ?: return Result.Error(NetworkError.UNKNOWN)
        } else {
            null
        }
        Log.i("pet", "reached udpateUse4")
        val mimeType = if (imageUri != null && imageUri != Uri.EMPTY) {
            getMimeType(imageUri, context) ?: "image/jpeg"
        } else {
            null
        }

        Log.i("pet", "reached udpateUser")

        val request = UpdateUserRequest(username = username, email = email)
        return when (val result =
            updateUserApi.updateUser(token, request, bytes, mimeType, fileName)) {
            is Result.Success -> {
                val response = result.data
                if (response.success && response.data != null) {
                    Result.Success(response.data.toUpdateUser())
                } else if (response.success) {
                    Result.Error(NetworkError.SERIALIZATION)
                } else {
                    Result.Error(NetworkError.SERVER_ERROR)
                }
            }

            is Result.Error -> Result.Error(result.error) // pass along actual error
        }
    }
}