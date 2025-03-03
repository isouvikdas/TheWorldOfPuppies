package com.example.theworldofpuppies.core.data.networking

import com.example.theworldofpuppies.BuildConfig

fun constructUrl(url: String): String {
    return when {
        url.startsWith(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.BASE_URL + url
        else -> BuildConfig.BASE_URL + "/" + url
    }
}