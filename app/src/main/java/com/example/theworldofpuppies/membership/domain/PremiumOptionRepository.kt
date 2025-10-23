package com.example.theworldofpuppies.membership.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface PremiumOptionRepository {
    suspend fun getPremiumOptions(): Result<List<PremiumOption>, NetworkError>
}