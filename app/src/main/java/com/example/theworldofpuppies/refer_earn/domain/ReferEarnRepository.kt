package com.example.theworldofpuppies.refer_earn.domain

import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result

interface ReferEarnRepository {

    suspend fun getWalletBalance(): Result<Double, NetworkError>
    suspend fun getReferralCode(): Result<String, NetworkError>
}