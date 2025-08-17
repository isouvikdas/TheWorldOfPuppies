package com.example.theworldofpuppies.services.grooming.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface GroomingRepository {
    suspend fun getGrooming(): Result<Grooming, NetworkError>
}