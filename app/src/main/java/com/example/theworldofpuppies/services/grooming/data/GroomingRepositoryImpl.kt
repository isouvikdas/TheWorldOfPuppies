package com.example.theworldofpuppies.services.grooming.data

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
    private val groomingApi: GroomingApi
) : GroomingRepository {

    override suspend fun getGrooming(): Result<Grooming, NetworkError> {
        return withContext(Dispatchers.IO) {
            when (val result = groomingApi.getServiceById()) {
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