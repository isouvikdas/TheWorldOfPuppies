package com.example.theworldofpuppies.services.grooming.data

import androidx.room.withTransaction
import com.example.theworldofpuppies.core.data.local.Database
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.services.grooming.data.mappers.toGrooming
import com.example.theworldofpuppies.services.grooming.data.remote.GroomingApi
import com.example.theworldofpuppies.services.grooming.domain.Grooming
import com.example.theworldofpuppies.services.grooming.domain.GroomingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroomingRepositoryImpl(
    private val groomingApi: GroomingApi,
    private val userRepository: UserRepository
) : GroomingRepository {

    override suspend fun getGrooming(): Result<Grooming, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)
        return withContext(Dispatchers.IO) {
            when (val result = groomingApi.getServiceById(token = token)) {
                is Result.Success -> {
                    val response = result.data
                    if (!response.success || response.data == null) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    } else {
                        Result.Success(response.data.toGrooming())
                    }
                }
                is Result.Error -> Result.Error(NetworkError.UNKNOWN)
            }
        }
    }

}