package com.example.theworldofpuppies.services.vet.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface VetRepository {

    suspend fun getVet(): Result<Vet, NetworkError>
}