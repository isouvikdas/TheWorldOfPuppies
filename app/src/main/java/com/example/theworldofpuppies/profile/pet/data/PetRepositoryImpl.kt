package com.example.theworldofpuppies.profile.pet.data

import android.content.Context
import android.net.Uri
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.getBytes
import com.example.theworldofpuppies.core.presentation.util.getFileName
import com.example.theworldofpuppies.core.presentation.util.getMimeType
import com.example.theworldofpuppies.profile.pet.data.mappers.toPet
import com.example.theworldofpuppies.profile.pet.data.remote.PetApi
import com.example.theworldofpuppies.profile.pet.data.remote.request.AddPetRequest
import com.example.theworldofpuppies.profile.pet.data.remote.request.UpdatePetRequest
import com.example.theworldofpuppies.profile.pet.domain.Pet
import com.example.theworldofpuppies.profile.pet.domain.PetRepository
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender

class PetRepositoryImpl(
    private val userRepository: UserRepository,
    private val api: PetApi,
    private val context: Context
): PetRepository {

    override suspend fun addPet(
        imageUri: Uri,
        name: String,
        breed: DogBreed,
        age: String,
        gender: Gender,
        aggression: Aggression,
        isVaccinated: Boolean,
        weight: String
    ): Result<Pet, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        val bytes = getBytes(imageUri, context)
            ?: return Result.Error(NetworkError.UNKNOWN)
        val fileName = getFileName(imageUri, context)
            ?: return Result.Error(NetworkError.UNKNOWN)
        val mimeType = getMimeType(imageUri, context) ?: "image/jpeg"

        val request = AddPetRequest(
            name = name,
            breed = breed,
            age = age,
            gender = gender,
            aggression = aggression,
            isVaccinated = isVaccinated,
            weight = weight
        )

        return when (val result = api.addPet(token, request, bytes, mimeType, fileName)) {
            is Result.Success -> {
                val response = result.data
                if (response.success && response.data != null) {
                    Result.Success(response.data.toPet())
                } else if (response.success) {
                    Result.Error(NetworkError.SERIALIZATION)
                } else {
                    Result.Error(NetworkError.SERVER_ERROR)
                }
            }
            is Result.Error -> Result.Error(result.error) // pass along actual error
        }
    }

    override suspend fun updatePet(
        imageUri: Uri?,
        id: String,
        name: String,
        breed: DogBreed,
        age: String,
        gender: Gender,
        aggression: Aggression,
        isVaccinated: Boolean,
        weight: String
    ): Result<Pet, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)


        val bytes = if (imageUri != null){
            getBytes(imageUri, context)
                ?: return Result.Error(NetworkError.UNKNOWN)
        } else {
            null
        }
        val fileName = if (imageUri != null) {
            getFileName(imageUri, context)
                ?: return Result.Error(NetworkError.UNKNOWN)
        } else {
            null
        }
        val mimeType = if (imageUri != null) {
            getMimeType(imageUri, context) ?: "image/jpeg"
        } else {
            null
        }

        val request = UpdatePetRequest(
            id = id,
            name = name,
            breed = breed,
            age = age,
            gender = gender,
            aggression = aggression,
            isVaccinated = isVaccinated,
            weight = weight
        )

        return when (val result = api.updatePet(token, request, bytes, mimeType, fileName)) {
            is Result.Success -> {
                val response = result.data
                if (response.success && response.data != null) {
                    Result.Success(response.data.toPet())
                } else if (response.success) {
                    Result.Error(NetworkError.SERIALIZATION)
                } else {
                    Result.Error(NetworkError.SERVER_ERROR)
                }
            }
            is Result.Error -> Result.Error(result.error) // pass along actual error
        }
    }


    override suspend fun getPets(petIds: List<String>): Result<List<Pet>, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        return when (val result = api.getPets(token, petIds)) {
            is Result.Success -> {
                val response = result.data
                if (response.success && response.data != null) {
                    val pets = response.data.map { petDto -> petDto.toPet() }
                    Result.Success(pets)
                } else if (response.success) {
                    Result.Error(NetworkError.SERIALIZATION)
                } else {
                    Result.Error(NetworkError.SERVER_ERROR)
                }
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun deletePet(petId: String): Result<Boolean, NetworkError> {
        val token = userRepository.getToken()
            ?: return Result.Error(NetworkError.UNAUTHORIZED)

        return when (val result = api.deletePet(token, petId)) {
            is Result.Success -> {
                val response = result.data
                if (response.success && response.data == null) {
                    Result.Success(true)
                } else if (!response.success) {
                    Result.Error(NetworkError.SERVER_ERROR)
                } else {
                    Result.Error(NetworkError.UNKNOWN)
                }
            }
            is Result.Error -> Result.Error(result.error)
        }
    }


}