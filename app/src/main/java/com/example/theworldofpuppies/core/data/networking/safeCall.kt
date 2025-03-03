package com.example.theworldofpuppies.core.data.networking

import android.util.Log
import com.example.theworldofpuppies.core.domain.util.NetworkError
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext
import com.example.theworldofpuppies.core.domain.util.Result
import io.ktor.util.network.UnresolvedAddressException

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        Log.e("SafeCall", "Unknown error occurred", e)
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}