package com.example.theworldofpuppies.shop.product.data.utils

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toList(jsonString: String): List<String> {
        return json.decodeFromString(jsonString)
    }
}