package com.example.theworldofpuppies.core.data.networking

import android.util.Log
import com.example.theworldofpuppies.core.domain.util.NetworkError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import com.example.theworldofpuppies.core.domain.util.Result

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError> {
    try {
        return when (response.status.value) {
            in 200..299 -> {
                try {
                    // Log success status and response
                    Log.i("ResponseToResult", "Success: ${response.status.value}")
                    Result.Success(response.body<T>())
                } catch (e: NoTransformationFoundException) {
                    // Log serialization error
                    Log.e("ResponseToResult", "Serialization error: ${e.message}", e)
                    Result.Error(NetworkError.SERIALIZATION)
                }
            }
            408 -> {
                Log.w("ResponseToResult", "Request timeout: ${response.status.value}")
                Result.Error(NetworkError.REQUEST_TIMEOUT)
            }
            401 -> {
                Log.w("ResponseToResult", "Request unauthorized: ${response.status.value}")
                Result.Error(NetworkError.UNAUTHORIZED)
            }
            429 -> {
                Log.w("ResponseToResult", "Too many requests: ${response.status.value}")
                Result.Error(NetworkError.TOO_MANY_REQUESTS)
            }
            in 500..599 -> {
                Log.e("ResponseToResult", "Server error: ${response.status.value}")
                Result.Error(NetworkError.SERVER_ERROR)
            }
            else -> {
                // Log unknown status
                Log.w("ResponseToResult", "Unknown error: ${response.status.value}")
                Result.Error(NetworkError.UNKNOWN)
            }
        }
    } catch (e: Exception) {
        // Catch any other unexpected exceptions
        Log.e("ResponseToResult", "Unexpected error: ${e.message}", e)
        return Result.Error(NetworkError.UNKNOWN)
    }
}
