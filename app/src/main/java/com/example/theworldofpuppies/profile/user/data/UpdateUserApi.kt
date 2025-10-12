package com.example.theworldofpuppies.profile.user.data

import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.profile.user.data.dto.UpdateUserDto
import com.example.theworldofpuppies.profile.user.data.request.UpdateUserRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UpdateUserApi(
    private val httpClient: HttpClient
) {

    suspend fun updateUser(
        token: String,
        request: UpdateUserRequest,
        bytes: ByteArray? = null,
        mimeType: String? = null,
        fileName: String? = null
    ): Result<ApiResponse<UpdateUserDto>, NetworkError> {
        return safeCall {
            httpClient.submitFormWithBinaryData(
                url = constructUrl("auth/user/update"), // Fixed endpoint
                formData = formData {
                    // Add JSON request as a part
                    append("request", Json.encodeToString(request), Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    })
                    if (bytes != null && mimeType != null && fileName != null) {
                        append("image", bytes, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                            append(HttpHeaders.ContentType, mimeType)
                        })

                    }
                }
            ) {
                header("Authorization", token)
            }
        }
    }
}