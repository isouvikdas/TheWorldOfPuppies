package com.example.theworldofpuppies.shop.product.data.utils

import androidx.room.TypeConverter
import com.example.theworldofpuppies.shop.product.domain.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    @TypeConverter
    fun fromImageList(images: List<Image>): String {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun toImageList(imagesString: String): List<Image> {
        val listType = object : TypeToken<List<Image>>() {}.type
        return Gson().fromJson(imagesString, listType) ?: emptyList()
    }

    // Optional: If you also need converter for single Image (for firstImage field)
    @TypeConverter
    fun fromImage(image: Image?): String? {
        return image?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toImage(imageString: String?): Image? {
        return imageString?.let {
            try {
                Gson().fromJson(it, Image::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

}