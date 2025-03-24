package com.example.theworldofpuppies.core.domain

data class Service(
    val localId: Long,
    val id: String,
    val name: String,
    val category: String,
    val plans: List<Plan>,
    val imageId: String? = null,
    val imageUri: String? = null
)
