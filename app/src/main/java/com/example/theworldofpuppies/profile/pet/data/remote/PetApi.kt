package com.example.theworldofpuppies.profile.pet.data.remote

import androidx.annotation.Nullable
import com.example.theworldofpuppies.core.data.networking.constructUrl
import com.example.theworldofpuppies.core.data.networking.safeCall
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.response.ApiResponse
import com.example.theworldofpuppies.profile.pet.data.remote.dto.PetDto
import com.example.theworldofpuppies.profile.pet.data.remote.request.AddPetRequest
import com.example.theworldofpuppies.profile.pet.data.remote.request.UpdatePetRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.util.InternalAPI
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PetApi(
    private val httpClient: HttpClient
) {

    suspend fun getPets(token: String, petIds: List<String>)
            : Result<ApiResponse<List<PetDto>>, NetworkError> {
        return safeCall {
            httpClient.get(
                urlString = constructUrl("auth/pet/all")
            ) {
                header("Authorization", token)
                setBody(petIds)
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun addPet(
        token: String,
        request: AddPetRequest,
        bytes: ByteArray,
        mimeType: String,
        fileName: String
    ): Result<ApiResponse<PetDto>, NetworkError> {
        return safeCall {
            httpClient.submitFormWithBinaryData(
                url = constructUrl("auth/pet/add"), // Fixed endpoint
                formData = formData {
                    // Add JSON request as a part
                    append("request", Json.encodeToString(request), Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    })

                    append("image", bytes, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        append(HttpHeaders.ContentType, mimeType)
                    })
                }
            ) {
                header("Authorization", token)
            }
        }
    }

    suspend fun updatePet(
        token: String,
        request: UpdatePetRequest,
        bytes: ByteArray? = null,
        mimeType: String? = null,
        fileName: String? = null
    ): Result<ApiResponse<PetDto>, NetworkError> {
        return safeCall {
            httpClient.put(constructUrl("auth/pet/update")) { // Use put() instead
                header("Authorization", token)
                setBody(
                    MultiPartFormDataContent(
                        formData {
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
                    )
                )
            }
        }
    }

    suspend fun deletePet(
        token: String,
        petId: String
    ): Result<ApiResponse<Nullable>, NetworkError> {
        return safeCall {
            httpClient.delete(
                constructUrl("auth/pet/$petId")
            ) {
                header("Authorization", token)
            }
        }
    }
}